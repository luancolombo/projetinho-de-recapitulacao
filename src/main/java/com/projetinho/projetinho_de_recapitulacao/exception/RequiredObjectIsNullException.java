package com.projetinho.projetinho_de_recapitulacao.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RequiredObjectIsNullException extends RuntimeException {

    public RequiredObjectIsNullException() {

        super("There is not allowed to persist a null object!");
    }

    public RequiredObjectIsNullException(String message) {

        super(message);
    }
}
