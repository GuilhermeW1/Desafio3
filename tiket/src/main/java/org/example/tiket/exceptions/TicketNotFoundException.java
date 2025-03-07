package org.example.tiket.exceptions;

public class TicketNotFoundException extends RuntimeException {
  public TicketNotFoundException(String message) {
    super(message);
  }
}
