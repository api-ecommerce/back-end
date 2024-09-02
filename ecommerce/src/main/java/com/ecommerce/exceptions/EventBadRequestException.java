package com.ecommerce.exceptions;

public class EventBadRequestException extends RuntimeException {

    public EventBadRequestException(){
        super("Erro na Requisição");
    }

    public EventBadRequestException(String mensagem){
        super(mensagem);
    }
}
