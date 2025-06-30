package com.sparta.java_02.domain.user.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class UserPurchaseResponse {

  Long id;

  String name;

  String email;

  Long purchaseId;

  BigDecimal purchaseTotalPrice;

  @QueryProjection
  public UserPurchaseResponse(
      Long id,
      String name,
      String email,
      Long purchaseId,
      BigDecimal purchaseTotalPrice) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.purchaseId = purchaseId;
    this.purchaseTotalPrice = purchaseTotalPrice;
  }
}
