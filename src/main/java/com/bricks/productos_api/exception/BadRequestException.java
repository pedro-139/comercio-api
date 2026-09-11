package com.bricks.productos_api.exception;

/**
 *  Se lanza para errores de negocio por datos de entrada inválidos.
 */
public class BadRequestException extends RuntimeException{

    public BadRequestException (String message){
        super(message);
    }
}
