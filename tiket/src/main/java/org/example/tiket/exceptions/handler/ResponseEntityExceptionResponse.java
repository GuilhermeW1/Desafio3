package org.example.tiket.exceptions.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.example.tiket.exceptions.EventNotFoundException;
import org.example.tiket.exceptions.ExceptionResponse;
import org.example.tiket.exceptions.TicketNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDate;

@ControllerAdvice
public class ResponseEntityExceptionResponse {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception e, WebRequest request) {
        ExceptionResponse res = new ExceptionResponse(LocalDate.now(), e.getMessage(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }

    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleTicketNotFoundException(TicketNotFoundException e, WebRequest request) {
        ExceptionResponse res = new ExceptionResponse(LocalDate.now(), e.getMessage(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    }

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleEventNotFoundException(EventNotFoundException e, WebRequest request) {
        ExceptionResponse res = new ExceptionResponse(LocalDate.now(), e.getMessage(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request, BindingResult bindingResult) {
        ExceptionResponse res = new ExceptionResponse(
                LocalDate.now(),
                e.getMessage(),
                "Invalid fields",
                bindingResult);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(res);
    }

}
