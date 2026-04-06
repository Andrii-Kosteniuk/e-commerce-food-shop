package com.ecommerce.commonexception.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccessRestrictedException extends AccessDeniedException {
    public AccessRestrictedException(String message) {
        super(message);
    }
}
