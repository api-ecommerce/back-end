package com.ecommerce.exceptions;

public class EventInternalServerErrorException extends RuntimeException {

    public EventInternalServerErrorException(){
        super("Erro no servidor.");

    }

    public EventInternalServerErrorException(String message){
        super(message);
    }
}
