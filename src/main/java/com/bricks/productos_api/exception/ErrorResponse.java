package com.bricks.productos_api.exception;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    // Mensaje sobre qué salió mal.
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

