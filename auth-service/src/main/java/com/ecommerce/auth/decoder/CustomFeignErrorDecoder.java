package com.ecommerce.auth.decoder;

import com.commonexception.exception.ResourceAccessDeniedException;
import com.commonexception.exception.ResourceAlreadyExistsException;
import com.commonexception.exception.ResourceNotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomFeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        int status = response.status();

        return switch (status) {
            case 403 -> throw new ResourceAccessDeniedException("Access denied");
            case 404 -> throw new ResourceNotFoundException("Resource not found");
            case 409 -> throw new ResourceAlreadyExistsException("Resource already exists");
            default -> defaultDecoder.decode(methodKey, response);
        };
    }
}
