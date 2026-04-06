package com.ecommerce.commonexception.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class CustomServerErrorException extends RuntimeException {

    public CustomServerErrorException(String message) {
        super(message);
    }
}
