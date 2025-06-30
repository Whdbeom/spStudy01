package com.sparta.java_02.domain.purchase.controller;

import com.sparta.java_02.common.response.ApiResponse;
import com.sparta.java_02.domain.purchase.entity.Purchase;
import com.sparta.java_02.domain.purchase.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/purchases")
public class PurchaseController {

  private final PurchaseService purchaseService;

  @GetMapping("/{purchaseId}")
  public ApiResponse<Purchase> getPurchase(@PathVariable Long purchaseId) {
    return ApiResponse.success(purchaseService.getPurchase(purchaseId));
  }

}
