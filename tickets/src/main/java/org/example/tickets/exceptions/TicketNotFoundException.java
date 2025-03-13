package org.example.tickets.exceptions;

public class TicketNotFoundException extends RuntimeException {
  public TicketNotFoundException(String message) {
    super(message);
  }
}
