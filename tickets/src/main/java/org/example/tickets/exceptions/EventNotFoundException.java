package org.example.tickets.exceptions;

public class EventNotFoundException extends RuntimeException {
  public EventNotFoundException(String message) {
    super(message);
  }
}
