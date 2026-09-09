package com.bricks.productos_api.controller;

import com.bricks.productos_api.exception.ResourceNotFoundException;
import com.bricks.productos_api.exception.ExternalServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
/** Convierte excepciones de la aplicación en respuestas HTTP consistentes. */
public class ApiExceptionHandler {
    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<Map<String, String>> handleExternalService(ExternalServiceException exception) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(Map.of("error", exception.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(ResourceNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("Datos inválidos");
        return ResponseEntity.badRequest().body(Map.of("error", message));
    }
}
