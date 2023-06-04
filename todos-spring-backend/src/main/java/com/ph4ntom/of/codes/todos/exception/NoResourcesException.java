package com.ph4ntom.of.codes.todos.exception;

import java.io.Serial;

public class NoResourcesException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 1L;

  public NoResourcesException(final String message) { super(message); }
}
