package com.ecommerce.feignconfig.decoder;

import com.ecommerce.commonexception.exception.CustomServerErrorException;
import com.ecommerce.commonexception.exception.ResourceAlreadyExistsException;
import org.springframework.security.authentication.BadCredentialsException;
import com.ecommerce.commonexception.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public class CustomFeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        int status = response.status();
        String serviceError = extractErrorMessage(response, methodKey);

        log.error("Feign error — method: {}, status: {}, message: {}",
                methodKey, status, serviceError);

        return switch (status) {
            case 401 -> new BadCredentialsException(serviceError);
            case 403 -> new org.springframework.security.access.AccessDeniedException(serviceError);
            case 404 -> new ResourceNotFoundException(serviceError);
            case 409 -> new ResourceAlreadyExistsException(serviceError);
            case 501 -> new CustomServerErrorException(serviceError);
            default -> defaultDecoder.decode(methodKey, response);
        };
    }

    private String extractErrorMessage(Response response, String methodKey) {
        try {
            if (response.body() == null) {
                return String.format("[%s] No response body", methodKey);
            }

            String body = new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8);
            JsonNode json = objectMapper.readTree(body);

            String message = extractMessage(json);

            return String.format("[%s] %s", methodKey, message);

        } catch (Exception e) {
            return String.format("[%s] Could not read error response", methodKey);
        }
    }

    private String extractMessage(JsonNode json) {

        if (json.has("details")) {
            String details = json.get("details").asText();

            if (details.startsWith("{")) {
                try {
                    JsonNode nested = objectMapper.readTree(details);
                    if (nested.has("details")) {
                        return nested.get("details").asText();
                    }
                } catch (Exception ignored) {
                                    }
            }

           return details;
        }

        if (json.has("message")) {
            return json.get("message").asText();
        }

        return json.toString();
    }
}