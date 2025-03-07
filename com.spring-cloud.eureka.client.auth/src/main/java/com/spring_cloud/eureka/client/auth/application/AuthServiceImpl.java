package com.spring_cloud.eureka.client.auth.application;

import com.spring_cloud.eureka.client.auth.infrastructure.UserRepository;
import com.spring_cloud.eureka.client.auth.model.User;
import com.spring_cloud.eureka.client.auth.model.UserRoleEnum;
import com.spring_cloud.eureka.client.auth.presentation.dto.SignUpRequest;
import com.spring_cloud.eureka.client.auth.security.jwt.JwtProperties;
import com.spring_cloud.eureka.client.auth.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl {


  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public String signUp(SignUpRequest signUpRequest) {
    String username = signUpRequest.getUsername();
    String password = passwordEncoder.encode(signUpRequest.getPassword());
    UserRoleEnum role = signUpRequest.getRole();

    if (userRepository.existsByUsername(username)) {
      throw new IllegalArgumentException("Username is already in use");
    }

    User user = userRepository.save(User.builder()
                                        .username(username)
                                        .password(password)
                                        .role(role)
                                        .build());

    return "success";
  }


}
