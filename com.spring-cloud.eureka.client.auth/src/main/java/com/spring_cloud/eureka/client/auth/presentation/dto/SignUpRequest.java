package com.spring_cloud.eureka.client.auth.presentation.dto;

import com.spring_cloud.eureka.client.auth.model.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

  private String username;
  private String password;
  private UserRoleEnum role;

}
