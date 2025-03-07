package com.spring_cloud.client.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    return http
        .csrf(ServerHttpSecurity.CsrfSpec::disable)  // ✅ CSRF 비활성화
        .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
        .authorizeExchange(auth -> auth
            .pathMatchers("/auth/signIn", "/auth/signUp").permitAll()
            .anyExchange().authenticated()
        )
        .build();
  }
}