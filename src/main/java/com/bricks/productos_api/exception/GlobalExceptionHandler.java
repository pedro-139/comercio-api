package com.bricks.productos_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


/*
 * Manejador global de errores para todos los controllers: en vez de que cada endpoint
 * tenga su propio try/catch, cualquier excepción lanzada desde un service o controller
 * termina en alguno de estos métodos, que la convierte en una respuesta HTTP
 */
@RestControllerAdvice //Controla las excepciones
public class GlobalExceptionHandler {

    // Se lanza cuando se busca un producto/categoría por ID y no existe -> HTTP 404.
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception){
        ErrorResponse errorResponse = new ErrorResponse(
                exception.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                "Recurso no encontrado"
        );
        return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
    }

    // Errores de negocio por datos inválidos que no cubre @Valid -> HTTP 400.
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException exception){
        ErrorResponse errorResponse = new ErrorResponse(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                "Solicitud incorrecta"
        );
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }

    // Falla la comunicación con la API pública de categorías -> HTTP 503.
    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ErrorResponse> handleExternalServiceException(ExternalServiceException exception){
        ErrorResponse errorResponse = new ErrorResponse(
                exception.getMessage(),
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "Servicio no disponible. Intente nuevamente más tarde."
        );
        return new ResponseEntity<>(errorResponse,HttpStatus.SERVICE_UNAVAILABLE);
    }

    // Se lanza cuando un @RequestBody con @Valid no pasa sus validaciones -> HTTP 400.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException exception){

        ErrorResponse errorResponse = new ErrorResponse(
                "Los datos enviados no son válidos",
                HttpStatus.BAD_REQUEST.value(),
                "Error de validación"
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Se dispara con cualquier otra excepción no prevista -> HTTP 500.

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Ocurrió un error interno. Intente nuevamente más tarde.",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error interno del servidor"
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
