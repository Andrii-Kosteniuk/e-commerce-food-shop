package com.ecommerce.commonexception.exception;


public class KafkaEventException extends RuntimeException {
    public KafkaEventException(String message, Throwable cause) {
        super(message, cause);
    }
}
