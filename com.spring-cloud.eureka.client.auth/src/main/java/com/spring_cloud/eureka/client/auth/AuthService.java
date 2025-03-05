package com.spring_cloud.eureka.client.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  @Value("${spring.application.name}")
  private String issuer;

  @Value("${service.jwt.access-expiration}")
  private Long accessExpiration;

  @Value("${service.jwt.secret-key}")
  private String secretKey;
  private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

  private SecretKey getSecretKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  };

  public String createAccessToken(String user_id){
    return Jwts.builder()
        .claim("user_id", user_id)
        .issuer(issuer)
        .issuedAt(new Date((System.currentTimeMillis())))
        .expiration(new Date(System.currentTimeMillis()+ accessExpiration))
        .signWith(getSecretKey(), signatureAlgorithm)
        .compact();
  }

}
