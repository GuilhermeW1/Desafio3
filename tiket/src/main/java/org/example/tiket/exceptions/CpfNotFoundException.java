package org.example.tiket.exceptions;

public class CpfNotFoundException extends RuntimeException {
    public CpfNotFoundException(String message) {
        super(message);
    }
}
