package org.example.tiket.exceptions;

public class EventNotFoundException extends RuntimeException {
  public EventNotFoundException(String message) {
    super(message);
  }
}
