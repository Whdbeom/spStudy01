package com.sparta.java_02.domain.purchase.service;

import com.sparta.java_02.common.constants.constants;
import com.sparta.java_02.common.enums.PurchaseStatus;
import com.sparta.java_02.common.exception.ServiceException;
import com.sparta.java_02.common.exception.ServiceExceptionCode;
import com.sparta.java_02.domain.product.entity.Product;
import com.sparta.java_02.domain.purchase.dto.PurchaseCancelResponse;
import com.sparta.java_02.domain.purchase.dto.PurchaseProductResponse;
import com.sparta.java_02.domain.purchase.entity.Purchase;
import com.sparta.java_02.domain.purchase.entity.PurchaseProduct;
import com.sparta.java_02.domain.purchase.repository.PurchaseProductRepository;
import com.sparta.java_02.domain.purchase.repository.PurchaseRepository;
import com.sparta.java_02.domain.user.repository.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PurchaseCancelService {

  private final UserRepository userRepository;
  private final PurchaseRepository purchaseRepository;
  private final PurchaseProductRepository purchaseProductRepository;

  @Transactional
  public PurchaseCancelResponse cancel(Long purchaseId, Long userId) {
    Purchase purchase = purchaseRepository.findByIdAndUser_Id(purchaseId, userId)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_DATA));

    validatePurchaseStatus(purchase);
    purchase.setStatus(PurchaseStatus.CANCELED);

    List<PurchaseProduct> purchaseProducts = purchaseProductRepository.findByPurchase_Id(
        purchaseId);

    restoreProductStock(purchaseProducts);

    // 결제 취소 API
    // 주문 취소 알람

    List<PurchaseProductResponse> purchaseProductResponses = getPurchaseProductResponses(
        purchaseProducts);

    return PurchaseCancelResponse.builder()
        .purchaseId(purchase.getId())
        .purchaseStatus(purchase.getStatus())
        .cancelledAt(LocalDateTime.now())
        .message(constants.PURCHASE_CANCEL_MESSAGE)
        .cancelledProducts(purchaseProductResponses)
        .build();
  }

  private void validatePurchaseStatus(Purchase purchase) {
    if (purchase.getStatus() != PurchaseStatus.PENDING) {
      throw new ServiceException(ServiceExceptionCode.CANNOT_CANCEL);
    }
  }

  private void restoreProductStock(List<PurchaseProduct> purchaseProducts) {
    for (PurchaseProduct purchaseProduct : purchaseProducts) {
      Product product = purchaseProduct.getProduct();
      product.increaseStock(purchaseProduct.getQuantity());
    }
  }

  private List<PurchaseProductResponse> getPurchaseProductResponses(
      List<PurchaseProduct> purchaseProducts) {
    return purchaseProducts.stream()
        .map((purchaseProduct) -> {
          Product product = purchaseProduct.getProduct();
          BigDecimal totalPrice = purchaseProduct.getPrice()
              .multiply(BigDecimal.valueOf(purchaseProduct.getQuantity()));

          return PurchaseProductResponse.builder()
              .productId(product.getId())
              .productName(product.getName())
              .quantity(purchaseProduct.getQuantity())
              .price(purchaseProduct.getPrice())
              .totalPrice(totalPrice)
              .build();
        }).toList();
  }
}
