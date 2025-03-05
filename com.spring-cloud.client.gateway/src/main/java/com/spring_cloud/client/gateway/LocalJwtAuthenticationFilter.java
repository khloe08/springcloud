package com.spring_cloud.client.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LocalJwtAuthenticationFilter implements GlobalFilter {

  @Value("${service.jwt.secret-key}")
  private String secretKey;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

    String path = exchange.getRequest().getURI().getPath();
    if(path.equals("/auth/signIn")){
      return chain.filter(exchange);
    }

    String token = exchange.getRequest().getHeaders().getFirst("Authorization");

    if (token != null && token.startsWith("Bearer ")) {
      token = token.substring(7);
    }else{
      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
      return exchange.getResponse().setComplete();
    }

    return chain.filter(exchange);
  }



}
