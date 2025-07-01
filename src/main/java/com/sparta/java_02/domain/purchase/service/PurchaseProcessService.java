package com.sparta.java_02.domain.purchase.service;

import com.sparta.java_02.common.exception.ServiceException;
import com.sparta.java_02.common.exception.ServiceExceptionCode;
import com.sparta.java_02.domain.product.entity.Product;
import com.sparta.java_02.domain.product.repository.ProductRepository;
import com.sparta.java_02.domain.purchase.dto.PurchaseProductRequest;
import com.sparta.java_02.domain.purchase.dto.PurchaseRequest;
import com.sparta.java_02.domain.purchase.entity.Purchase;
import com.sparta.java_02.domain.purchase.entity.PurchaseProduct;
import com.sparta.java_02.domain.purchase.repository.PurchaseProductRepository;
import com.sparta.java_02.domain.purchase.repository.PurchaseRepository;
import com.sparta.java_02.domain.user.entity.User;
import com.sparta.java_02.domain.user.repository.UserRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PurchaseProcessService {

  private final UserRepository userRepository;
  private final PurchaseRepository purchaseRepository;
  private final ProductRepository productRepository;
  private final PurchaseProductRepository purchaseProductRepository;

  public Purchase getPurchase(Long purchaseId) {
    User user = userRepository.findById(1L)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_DATA));

    return purchaseRepository.findById(purchaseId)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_DATA));
  }

  @Transactional
  public Purchase createPurchase(PurchaseRequest request) {
    User user = userRepository.findById(request.getUserId())
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_USER));

    Purchase purchase = purchaseRepository.save(Purchase.builder()
        .user(user)
        .build());

    BigDecimal totalPrice = BigDecimal.ZERO;
    List<PurchaseProduct> purchaseProducts = new ArrayList<>();

    for (PurchaseProductRequest productRequest : request.getPurchaseProducts()) {
      Product product = productRepository.findById(productRequest.getProductId())
          .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_DATA));

      if (productRequest.getQuantity() > product.getStock()) {
        throw new ServiceException(ServiceExceptionCode.NOT_FOUND_DATA);
      }

      product.reduceStock(productRequest.getQuantity());
      PurchaseProduct purchaseProduct = PurchaseProduct.builder()
          .product(product)
          .purchase(purchase)
          .quantity(productRequest.getQuantity())
          .price(product.getPrice())
          .build();

      purchaseProducts.add(purchaseProduct);
      totalPrice = totalPrice.add(
          product.getPrice().multiply(BigDecimal.valueOf(productRequest.getQuantity())));

    }

    purchase.setTotalPrice(totalPrice);
    purchaseProductRepository.saveAll(purchaseProducts);

    return null;
  }

//  private Purchase savePurchase(Long userId) {
//
//  }
//
//  private List<PurchaseProduct> createPurchaseItem(List<PurchaseProductRequest> itemRequests,
//      Purchase purchase) {
//
//  }
//
//  private void validateStock(Product product, int requestQuantity) {
//
//  }


}
