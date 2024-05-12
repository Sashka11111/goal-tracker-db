package com.liamtseva.domain.exception;

import java.io.Serial;

public class EntityNotFoundException extends Exception {

  public EntityNotFoundException(String message) {
    super(message);
  }

  public EntityNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
