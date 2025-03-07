package com.spring_cloud.client.gateway.exception;

public abstract class BaseException extends RuntimeException {
  private final int errorCode;

  public BaseException(int errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

  public int getErrorCode() {
    return errorCode;
  }
}
