package com.spring_cloud.eureka.client.auth.model;

public enum UserRoleEnum {
  MASTER(Authority.MASTER),
  MANAGER(Authority.MANAGER),
  CUSTOMER(Authority.CUSTOMER);

  private final String authority;

  UserRoleEnum(String authority) { this.authority = authority; }

  public String getAuthority(){ return this.authority; }

  public static class Authority{
    public static final String MASTER = "ROLE_MASTER";
    public static final String MANAGER = "ROLE_MANAGER";
    public static final String CUSTOMER = "ROLE_CUSTOMER";
  }
}
