package com.commonexception.exception;

public class ResourceAlreadyExistsException extends RuntimeException  {

    public ResourceAlreadyExistsException(String message) {
        super(message);
    }

    public ResourceAlreadyExistsException(Class<?> clazz) {
        super(clazz.getSimpleName() + " already exists");
    }


}
