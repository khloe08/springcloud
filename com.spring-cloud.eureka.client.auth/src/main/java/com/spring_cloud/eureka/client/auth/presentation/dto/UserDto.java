package com.spring_cloud.eureka.client.auth.presentation.dto;

import com.spring_cloud.eureka.client.auth.model.UserRoleEnum;

public record UserDto (Long id,
                       String username,
                       String password,
                       UserRoleEnum role){

    public static UserDto of(Long id, String username, String password, UserRoleEnum role) {
        return new UserDto(id, username, password, role);
    }
}