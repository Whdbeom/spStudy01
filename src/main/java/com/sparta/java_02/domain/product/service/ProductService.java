package com.sparta.java_02.domain.product.service;

import com.sparta.java_02.domain.product.dto.ProductResponse;
import com.sparta.java_02.domain.product.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;

  @Transactional(readOnly = true)
  public List<ProductResponse> getAllProducts() {
    return productRepository.findAll().stream()
        .map((product) -> ProductResponse.builder()
            .id(product.getId())
//            .categoryId(product.getCategoryId())
            .description(product.getDescription())
            .price(product.getPrice())
            .stock(product.getStock())
            .build())
        .toList();
  }


}
