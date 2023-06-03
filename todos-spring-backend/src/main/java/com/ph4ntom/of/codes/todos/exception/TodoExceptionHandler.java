package com.ph4ntom.of.codes.todos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class TodoExceptionHandler {

  @ExceptionHandler({ResourceNotFoundException.class, NoResourcesException.class})
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public ErrorMessage runtimeExceptionHandler(final RuntimeException runtimeException,
                                              final WebRequest request) {

    return new ErrorMessage(HttpStatus.NOT_FOUND.value(), runtimeException.getMessage(),
                            request.getDescription(false));
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorMessage globalExceptionHandler(final Exception exception, final WebRequest request) {

    return new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
               exception.getMessage(), request.getDescription(false));
  }
}
