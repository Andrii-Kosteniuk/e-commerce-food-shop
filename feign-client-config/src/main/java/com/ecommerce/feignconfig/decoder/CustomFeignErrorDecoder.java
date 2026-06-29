package com.ecommerce.feignconfig.decoder;

import com.ecommerce.commonexception.exception.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class CustomFeignErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {

        String responseBody = null;

        try {
            if (response.body() != null) {
                responseBody = new String(
                        response.body().asInputStream().readAllBytes(),
                        StandardCharsets.UTF_8
                );
            }

        } catch (IOException e) {
            log.error("Failed to read Feign error response body", e);
        }

        String extractedMessage = extractMessage(responseBody);

        log.error("Error — method: {}, status: {}, body: {}", methodKey, response.status(), responseBody);

        return switch (response.status()) {
            case 400 -> new IllegalArgumentException(extractedMessage);
            case 401 -> new BadCredentialsException(extractedMessage);
            case 403 -> new AccessDeniedException(extractedMessage);
            case 404 -> new ResourceNotFoundException(extractedMessage);
            case 409 -> new ResourceAlreadyExistsException(extractedMessage);
            case 408, 429, 500, 502, 503, 504 ->
                    new RetryableException(
                            response.status(),
                            !extractedMessage.isBlank() ? extractedMessage : "Retryable Feign error",
                            response.request().httpMethod(),
                            (Long) null,
                            response.request());
            default -> new FeignClientException(!extractedMessage.isBlank() ? extractedMessage : "Unknown Feign error");
        };
    }

    private String extractMessage(String body) {
        if (body == null || body.isBlank()) {
            return "";
        }

        try {
            JsonNode node = objectMapper.readTree(body);

            if (node.has("message")) {
                return node.get("message").asText();
            }

            if (node.has("error")) {
                return node.get("error").asText();
            }

            if (node.has("details")) {
                return node.get("details").asText();
            }

        } catch (Exception exception) {
            log.error("Failed to parse Feign error response body", exception);
        }

        return body;
    }
}