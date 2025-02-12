package com.admoliveira.exchangerate.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handle(ConstraintViolationException ex) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnavailableRatesException.class)
    public ResponseEntity<Map<String, String>> handle(UnavailableRatesException ex) {
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(JwtSubjectNotFound.class)
    public ResponseEntity<Map<String, String>> handle(JwtSubjectNotFound ex) {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

}
