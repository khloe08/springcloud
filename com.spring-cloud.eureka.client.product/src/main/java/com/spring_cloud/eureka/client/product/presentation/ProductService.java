package com.spring_cloud.eureka.client.product.presentation;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {


  public String getProduct(String productId) {

    if ("111".equals(productId)) {
      throw new RuntimeException("Empty response body");
    }
    return "Sample Product";
  }



}
