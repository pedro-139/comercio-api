package com.bricks.productos_api.exception;

import lombok.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private int statusCode;
    private LocalDateTime time;
    private String errorDetails;

    public ErrorResponse(String message, int statusCode, String errorDetails) {
        this.message = message;
        this.statusCode = statusCode;
        this.errorDetails = errorDetails;
    }
}

