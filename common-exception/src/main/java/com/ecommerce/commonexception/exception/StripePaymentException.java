package com.ecommerce.commonexception.exception;

public class StripePaymentException extends RuntimeException {

    public StripePaymentException(String message) {
        super(message);
    }

}
