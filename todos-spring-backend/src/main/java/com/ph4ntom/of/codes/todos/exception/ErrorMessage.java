package com.ph4ntom.of.codes.todos.exception;

import java.util.Date;

public record ErrorMessage(int statusCode, Date timeStamp, String message, String description) {

  public ErrorMessage(final int statusCode, final String message, final String description) {

    this(statusCode, new Date(), message, description);
  }
}
