package com.bricks.productos_api.exception;

/** Se lanza cuando falla la comunicación con el servicio externo de categorías. */
public class ExternalServiceException extends RuntimeException {
    public ExternalServiceException(String message) {
        super(message);
    }
}
