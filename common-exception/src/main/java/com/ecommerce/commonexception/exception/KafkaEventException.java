package com.ecommerce.commonexception.exception;


import org.apache.kafka.common.KafkaException;

public class KafkaEventException extends KafkaException {
    public KafkaEventException(String message, Throwable cause) {
        super(message, cause);
    }
}
