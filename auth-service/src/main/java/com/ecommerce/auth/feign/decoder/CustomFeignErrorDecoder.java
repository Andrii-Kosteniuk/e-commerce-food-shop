package com.ecommerce.auth.feign.decoder;

import com.ecommerce.commonexception.exception.ResourceAccessDeniedException;
import com.ecommerce.commonexception.exception.ResourceAlreadyExistsException;
import com.ecommerce.commonexception.exception.ResourceNotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.security.authentication.BadCredentialsException;

public class CustomFeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        int status = response.status();

        return switch (status) {
            case 401 -> new BadCredentialsException("Bad credentials");
            case 403 -> new ResourceAccessDeniedException("Access denied");
            case 404 -> new ResourceNotFoundException("Resource not found");
            case 409 -> new ResourceAlreadyExistsException("Resource already exists");
            default -> defaultDecoder.decode(methodKey, response);
        };
    }
}
