package com.spring_cloud.eureka.client.order;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="product")  //eureka client name
public interface ProductClient {

  @GetMapping("/product/{id}")
  @CircuitBreaker(name="circuit", fallbackMethod="fallbackProduct")
  String getProduct(@PathVariable("id") String id);


default String fallbackProduct(String id, Throwable t) {
return "Fallback product";
}


}
