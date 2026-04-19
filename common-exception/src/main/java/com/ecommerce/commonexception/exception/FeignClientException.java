package com.ecommerce.commonexception.exception;

public class FeignClientException extends RuntimeException {

    public FeignClientException(String message) {
        super(message);
    }
}