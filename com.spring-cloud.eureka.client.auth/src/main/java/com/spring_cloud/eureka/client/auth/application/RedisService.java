package com.spring_cloud.eureka.client.auth.application;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

  private final StringRedisTemplate redisTemplate;


  // 블랙리스트에 JWT 추가 (만료 시간과 동일한 TTL 설정)
  public void addToBlacklist(String token, long expirationTime){
    redisTemplate.opsForValue().set("blackList: "+token, "blackListed", expirationTime, TimeUnit.MILLISECONDS);
    deleteRefreshToken(token);
  }

  // JWT가 블랙리스트에 있는지 확인
  public boolean isBlacklisted(String token){
    return redisTemplate.hasKey("blackList: "+token);
  }

  // Refresh Token 저장 (로그인 시)
  public void storeRefreshToken(String userId, String refreshToken, long expirationTime) {
    redisTemplate.opsForValue().set("refresh_token: " + userId, refreshToken, expirationTime, TimeUnit.MILLISECONDS);
  }

  public String getRefreshToken(String userId) {
    return redisTemplate.opsForValue().get("refresh_token: " + userId);
  }


  public void deleteRefreshToken(String userId) {
    redisTemplate.delete("refresh_token: " + userId);
  }


}
