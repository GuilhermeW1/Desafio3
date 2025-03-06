package org.example.events.exceptions;

public class CepNotFoundException extends RuntimeException {
  public CepNotFoundException(String message) {
    super(message);
  }
}
