package com.spring_cloud.client.gateway.exception;

public class NotExistsAuthorization extends BaseException {
  public NotExistsAuthorization() {
    super(701, "Authorization header is missing or invalid.");
  }
}
