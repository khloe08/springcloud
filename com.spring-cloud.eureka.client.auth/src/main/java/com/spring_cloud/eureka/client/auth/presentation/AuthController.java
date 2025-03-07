package com.spring_cloud.eureka.client.auth.presentation;

import com.spring_cloud.eureka.client.auth.application.AuthServiceImpl;
import com.spring_cloud.eureka.client.auth.presentation.dto.AuthResponse;
import com.spring_cloud.eureka.client.auth.presentation.dto.SignInRequest;
import com.spring_cloud.eureka.client.auth.presentation.dto.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthServiceImpl authService;


  @PostMapping("/signUp")
  public ResponseEntity<String> signUp(@RequestBody SignUpRequest signUpRequest){
    String userId = authService.signUp(signUpRequest);
    return ResponseEntity.ok("signUp Success : " + userId);
  }

 /* @PostMapping("/signIn")
  public ResponseEntity<?> createAuthenticationToken(@RequestBody SignInRequest signInRequest){
    String token = authService.signIn(signInRequest.getUsername(), signInRequest.getPassword());
    return ResponseEntity.ok(new AuthResponse(token));
  }*/

}
