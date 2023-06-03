package com.ph4ntom.of.codes.todos.exception;

import java.io.Serial;

public class ResourceNotFoundException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 1L;

  public ResourceNotFoundException(final String message) { super(message); }
}
