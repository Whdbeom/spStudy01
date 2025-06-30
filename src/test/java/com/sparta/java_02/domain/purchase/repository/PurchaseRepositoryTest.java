package com.sparta.java_02.domain.purchase.repository;

import com.sparta.java_02.common.enums.PurchaseStatus;
import com.sparta.java_02.domain.purchase.entity.Purchase;
import com.sparta.java_02.domain.user.entity.User;
import com.sparta.java_02.domain.user.repository.UserRepository;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public class PurchaseRepositoryTest {

  @Autowired
  private PurchaseRepository purchaseRepository;

  @Autowired
  private UserRepository userRepository;

  @Test
  void save() {
    User user = userRepository.save(User.builder()
        .name("홍길동")
        .email("asdf@asdfa.com")
        .passwordHash("1")
        .build());

    Purchase purchase = Purchase.builder()
        .user(user)
        .totalPrice(BigDecimal.valueOf(1000))
        .status(PurchaseStatus.PENDING)
        .build();

    Purchase savePurchase = purchaseRepository.save(purchase);

    savePurchase.setStatus(PurchaseStatus.COMPLETION);
    purchaseRepository.save(savePurchase);
  }

  @Test
  void edit() {
    User user = userRepository.save(User.builder()
        .name("홍길동2")
        .email("asdf2@asdfa.com")
        .passwordHash("1")
        .build());

    Purchase purchase = Purchase.builder()
        .user(user)
        .totalPrice(BigDecimal.valueOf(1000))
        .status(PurchaseStatus.PENDING)
        .build();

    Purchase savePurchase = purchaseRepository.save(purchase);

    savePurchase.setStatus(PurchaseStatus.COMPLETION);
    purchaseRepository.save(savePurchase);
  }

  @Test
  void delete() {
    User user = userRepository.save(User.builder()
        .name("홍길동3")
        .email("asdf3@asdfa.com")
        .passwordHash("1")
        .build());

    Purchase purchase = Purchase.builder()
        .user(user)
        .totalPrice(BigDecimal.valueOf(1000))
        .status(PurchaseStatus.PENDING)
        .build();

    Purchase savePurchase = purchaseRepository.save(purchase);
    purchaseRepository.delete(savePurchase);

//    List<Purchase> purchases = new ArrayList<>();
//    purchaseRepository.deleteAll();
  }

  @Test
  void select() {
    List<Purchase> purchases = purchaseRepository.findAll();

    System.out.println(purchases);

    for (Purchase purchase : purchases) {
      System.out.println(purchase.getUser().getName());
    }
  }
}
