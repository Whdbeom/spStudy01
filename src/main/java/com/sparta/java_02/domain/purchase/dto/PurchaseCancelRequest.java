package com.sparta.java_02.domain.purchase.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseCancelRequest {

  Long purchaseId;

  Long userId;


}
