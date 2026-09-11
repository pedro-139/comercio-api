package com.bricks.productos_api.exception;

/**
 * Se lanza cuando se busca un recurso (producto o categoría) por ID y no existe.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

}
