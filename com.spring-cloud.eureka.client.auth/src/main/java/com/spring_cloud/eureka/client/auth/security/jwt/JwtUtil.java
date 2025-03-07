package com.spring_cloud.eureka.client.auth.security.jwt;


import com.spring_cloud.eureka.client.auth.application.RedisService;
import com.spring_cloud.eureka.client.auth.model.UserRoleEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

import io.jsonwebtoken.*;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class JwtUtil {


    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String BEARER_PREFIX = "Bearer ";
    public final RedisService redisService;


    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    private final JwtProperties jwtProperties;

    public JwtUtil(JwtProperties jwtProperties, RedisService redisService) {
        this.jwtProperties = jwtProperties;
        this.redisService = redisService;
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    };

    //accessToken 재발급
    public String refreshAccessToken(String username,  UserRoleEnum role,  String refreshToken){
        //Redis에 저장된 Refresh Token 과 비교
        String storedRefreshToken = redisService.getRefreshToken(username);
        if(storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)){
            throw new RuntimeException("Invalid Refresh Token");
        }

        //새로운 access token 발급
        return createToken(username,role);

    }


    //accessToken 생성
    public String createToken(String username, UserRoleEnum role) {
        Date date = new Date();

        return BEARER_PREFIX +
            Jwts.builder()
                .claim("user_id", username)
                .claim("role", role)
                .setExpiration(new Date(date.getTime() + jwtProperties.getTokenTime()))
                .setIssuedAt(date)
                .signWith(getSecretKey(), signatureAlgorithm)
                .compact();
    }


    //refresh Token 생성(로그인 시 사용)
    public String createRefreshToken(String username, UserRoleEnum role) {

        Date date = new Date();
        return BEARER_PREFIX +
            Jwts.builder()
                .setSubject(username)
                .claim(AUTHORIZATION_KEY, role)
                .setExpiration(new Date(date.getTime() + jwtProperties.getRefreshTokenTime()))
                .setIssuedAt(date)
                .signWith(getSecretKey(), signatureAlgorithm)
                .compact();


    }

    //로그아웃 시 jwt만료 시간확인
    public long getExpiration(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(jwtProperties.getSecretKey())
            .parseClaimsJws(token)
            .getBody();

        Date expiration = claims.getExpiration();
        return expiration.getTime() - System.currentTimeMillis(); // 남은 유효 시간
    }


    public Claims parseClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token).getBody();
    }


}