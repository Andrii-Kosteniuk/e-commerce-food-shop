package com.commonexception.exception;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String resource) {
        super( String.format("%s not found", resource));
    }
}
