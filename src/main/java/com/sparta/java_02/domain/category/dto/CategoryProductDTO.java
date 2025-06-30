package com.sparta.java_02.domain.category.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CategoryProductDTO {

  String categoryName;

  String productName;

  BigDecimal price;

  Integer stock;

  @QueryProjection
  public CategoryProductDTO(String categoryName, String productName, BigDecimal price,
      Integer stock) {
    this.categoryName = categoryName;
    this.productName = productName;
    this.price = price;
    this.stock = stock;
  }
}
