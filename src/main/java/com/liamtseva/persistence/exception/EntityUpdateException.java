package com.liamtseva.persistence.exception;

import java.io.Serial;

public class EntityUpdateException extends RuntimeException {

  @Serial private static final long serialVersionUID = 6047515523972627459L;

  public EntityUpdateException(String message) {
    super(message);
  }
}