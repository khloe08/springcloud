package com.spring_cloud.eureka.client.auth.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "users")
@NoArgsConstructor
@Data
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String username;
  private String password;
  private UserRoleEnum role;

  @Builder
  private User(String username, String password, UserRoleEnum role) {
    this.username = username;
    this.password = password;
    this.role = role;
  }

}
