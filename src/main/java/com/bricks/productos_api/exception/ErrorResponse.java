package com.bricks.productos_api.exception;

import lombok.*;
import java.time.LocalDateTime;

/** Cuerpo JSON estándar que se devuelve en toda respuesta de error (ver {@link GlobalExceptionHandler}). */
@Data
public class ErrorResponse {
    /** Mensaje legible sobre qué salió mal. */
    private String message;

    private int statusCode;

    private LocalDateTime time;

    private String errorDetails;

    public ErrorResponse(String message, int statusCode, String errorDetails) {
        this.message = message;
        this.statusCode = statusCode;
        this.time = LocalDateTime.now();
        this.errorDetails = errorDetails;
    }
}

