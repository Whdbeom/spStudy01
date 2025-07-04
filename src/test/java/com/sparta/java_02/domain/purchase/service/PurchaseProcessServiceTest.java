package com.sparta.java_02.domain.purchase.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.java_02.common.enums.PurchaseStatus;
import com.sparta.java_02.domain.product.entity.Product;
import com.sparta.java_02.domain.product.repository.ProductRepository;
import com.sparta.java_02.domain.purchase.dto.PurchaseProductRequest;
import com.sparta.java_02.domain.purchase.entity.Purchase;
import com.sparta.java_02.domain.purchase.repository.PurchaseProductRepository;
import com.sparta.java_02.domain.purchase.repository.PurchaseRepository;
import com.sparta.java_02.domain.user.entity.User;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class PurchaseProcessServiceTest {

  @InjectMocks
  private PurchaseProcessService purchaseProcessService; // 테스트 대상

  @Mock
  private PurchaseRepository purchaseRepository;

  @Mock
  private ProductRepository productRepository;

  @Mock
  private PurchaseProductRepository purchaseProductRepository;

  private User testUser;
  private Purchase testPurchase;
  private Product testProduct;

  @BeforeEach
  void setUp() {
    testUser = User.builder()
        .name("테스트사용자")
        .email("test@example.com")
        .passwordHash("hashedPassword")
        .build();

    ReflectionTestUtils.setField(testUser, "id", 1L);

    testProduct = Product.builder()
        .name("노트북")
        .price(new BigDecimal("1000000"))
        .stock(10)
        .build();

    ReflectionTestUtils.setField(testProduct, "id", 1L);

    testPurchase = Purchase.builder()
        .user(testUser)
        .totalPrice(BigDecimal.ZERO)
        .status(PurchaseStatus.PENDING)
        .build();

    ReflectionTestUtils.setField(testPurchase, "id", 1L);
  }

  @Test
  @DisplayName("재고가 충분한 상품을 구매하면 재고가 감소하고 구매가 성공한다.")
  void process_should_decreaseStockAndSucceed_when_productInStock() {
    // Given
    PurchaseProductRequest purchaseItem = new PurchaseProductRequest();
    ReflectionTestUtils.setField(purchaseItem, "productId", 1L);
    ReflectionTestUtils.setField(purchaseItem, "quantity", 1);

    List<PurchaseProductRequest> purchaseItems = List.of(purchaseItem);

    when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
    when(purchaseRepository.save(any(Purchase.class))).thenReturn(testPurchase);
    when(purchaseProductRepository.saveAll(anyList())).thenReturn(Collections.emptyList());

    // When
    Purchase purchase = purchaseProcessService.process(testUser, purchaseItems);

    // Then
    assertThat(purchase).isNotNull(); // null 이면 테스트 통과 X
    assertThat(purchase.getTotalPrice()).isEqualTo(new BigDecimal("1000000"));
    assertThat(testProduct.getStock()).isEqualTo(9);

    verify(productRepository).findById(1L);
    verify(purchaseRepository).save(any(Purchase.class));
    verify(purchaseProductRepository).saveAll(anyList());
  }

  @Test
  @DisplayName("존재하지 않는 상품을 구매하려고 하면 NoSuchElementException이 발생한다")
  void process_should_throwNoSuchElementException_when_nonExistentProduct() {
    // Given
    PurchaseProductRequest purchaseItem = new PurchaseProductRequest();
    ReflectionTestUtils.setField(purchaseItem, "productId", 9999L);
    ReflectionTestUtils.setField(purchaseItem, "quantity", 1);

    List<PurchaseProductRequest> purchaseItems = List.of(purchaseItem);

    when(productRepository.findById(9999L)).thenReturn(Optional.empty());
    when(purchaseRepository.save(any(Purchase.class))).thenReturn(testPurchase);

    // When & Then
    assertThrows(NoSuchElementException.class,
        () -> purchaseProcessService.process(testUser, purchaseItems));

    verify(productRepository).findById(9999L);

    verify(purchaseRepository).save(any(Purchase.class));
    verify(purchaseProductRepository, never()).saveAll(anyList());
  }
}