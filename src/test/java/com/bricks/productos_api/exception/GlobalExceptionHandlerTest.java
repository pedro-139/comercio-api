package com.bricks.productos_api.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/** Prueba el consejo global sin iniciar Spring y asegura que conserva el estado HTTP del dominio. */
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void recursoNoEncontrado_debeResponder404ConTimestamp() {
        var respuesta = handler.handleResourceNotFoundException(
                new ResourceNotFoundException("Producto con ID 99 no encontrado"));

        assertEquals(404, respuesta.getStatusCode().value());
        assertNotNull(respuesta.getBody().getTime());
        assertEquals("Recurso no encontrado", respuesta.getBody().getErrorDetails());
    }
}
