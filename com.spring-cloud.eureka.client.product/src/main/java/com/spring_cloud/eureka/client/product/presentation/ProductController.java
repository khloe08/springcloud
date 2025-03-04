package com.spring_cloud.eureka.client.product.presentation;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Value("${server.port}")
    private String port;

    @GetMapping("/product/{id}")
    public String getProduct(@PathVariable("id") String id){
        return productService.getProduct(id);
    }




}
