package com.spring_cloud.client.gateway.exception;

public class AccessTokenExpiredException extends BaseException {
  public AccessTokenExpiredException() {
    super(702, "Access token has expired.");
  }
}

