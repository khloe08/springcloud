package com.spring_cloud.eureka.client.auth.presentation.dto;

import com.spring_cloud.eureka.client.auth.model.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignInRequest {
  private String username;
  private String password;


}
