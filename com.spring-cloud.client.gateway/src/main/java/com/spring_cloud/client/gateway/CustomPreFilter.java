package com.spring_cloud.client.gateway;

import com.spring_cloud.client.gateway.Login.RedisService;
import com.spring_cloud.client.gateway.exception.AccessTokenExpiredException;
import com.spring_cloud.client.gateway.exception.BaseException;
import com.spring_cloud.client.gateway.exception.NotExistsAuthorization;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.ws.rs.core.HttpHeaders;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "JWT 인가 필터")
public class CustomPreFilter  implements GlobalFilter, Ordered {

  @Value("${service.jwt.secret-key}")
  private String secretKey;
  private static final String AUTH_TYPE = "Bearer ";

  private final RedisService redisService;


  private SecretKey getSecretKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  };

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

    String path = exchange.getRequest().getURI().getPath();

    if(path.equals("/auth/signIn")||path.equals("/auth/signUp")){
      log.info("********** info " +exchange);
      return chain.filter(exchange);
    }

    try {
      //토큰가져오기
      String authorization = getJwtFromHeader(exchange);

      //검증
      if (!StringUtils.hasText(authorization) || !validateToken(authorization)) {
        throw new NotExistsAuthorization();
      }

        String jwtToken = parseAuthorizationToken(authorization);

        //유효기간확인
        if (isValidateExpire(jwtToken)) {
          throw new AccessTokenExpiredException();
        }

        // 블랙리스트 확인
        if (redisService.isBlacklisted(authorization)) {
          throw new AccessTokenExpiredException();
        }

        // ✅ JWT 검증 성공 후 요청 헤더에 사용자 정보 추가
        ServerWebExchange modifiedExchange = exchange.mutate()
            .request(exchange.getRequest().mutate()
                .header("X-User-ID", getUserIdFromToken(jwtToken))
                .build())
            .build();

        return chain.filter(modifiedExchange);





    } catch (BaseException e) {
      return sendErrorResponse(exchange, e.getErrorCode(), e);
    } catch (Exception e) {
      return sendErrorResponse(exchange, 999, e);
    }


  }

  private String getUserIdFromToken(String jwtToken) {
    return Jwts.parserBuilder()
        .setSigningKey(getSecretKey())
        .build()
        .parseClaimsJws(jwtToken)
        .getBody()
        .getSubject();
  }


  private Mono<Void> sendErrorResponse(ServerWebExchange exchange, int errorCode, Exception e) {
    String errorBody = "{\"code\": " + errorCode + ", \"message\": \"" + e.getMessage() + "\"}";

    ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(HttpStatus.UNAUTHORIZED);
    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
    DataBuffer buffer = response.bufferFactory().wrap(errorBody.getBytes(StandardCharsets.UTF_8));
    return response.writeWith(Flux.just(buffer));
  }


  private String parseAuthorizationToken(String authorization) {
    return authorization.replace(AUTH_TYPE, "").trim();
  }

  public boolean validateToken(String token) {
    try {
      parseClaims(token);
      return true;
    } catch (ExpiredJwtException e) {
      log.error("Expired JWT token, 만료된 JWT token 입니다.");
      throw e;
    } catch (SecurityException | MalformedJwtException e) {
      log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
    } catch (UnsupportedJwtException e) {
      log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
    } catch (IllegalArgumentException e) {
      log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
    }
    return false;
  }

  public Claims parseClaims(String token) {
    return Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token).getBody();
  }


  public String getJwtFromHeader(ServerWebExchange exchange) {
    List<String> authorizations = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
    if (authorizations == null || authorizations.isEmpty()) { // ✅ Null 체크 추가
      throw new NotExistsAuthorization();
    }
    return authorizations.stream()
        .filter(this::isBearerType)
        .findFirst()
        .orElseThrow(NotExistsAuthorization::new);
  }

  private boolean isBearerType(String authorization) {
    return authorization.startsWith(AUTH_TYPE);
  }


  private boolean isValidateExpire(String jwtToken) {
    Date expiration = Jwts.parserBuilder().setSigningKey(getSecretKey())
        .build()
        .parseClaimsJws(jwtToken)
        .getBody()
        .getExpiration();
    return expiration.before(new Date());
  }

  @Override
  public int getOrder() {
    return -1;
  }
}
