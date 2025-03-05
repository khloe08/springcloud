package com.spring_cloud.eureka.client.product.presentation;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RefreshScope
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Value("${server.port}")
    private String port;

    @Value("${message}")
    private String message;

    @GetMapping("/product/{id}")
    public String getProduct(@PathVariable("id") String id){
        return productService.getProduct(id);
    }

    @GetMapping("/product")
    public String getMessage(){
        return "Product info From port : " + port + " Message : " + message;
    }




}
