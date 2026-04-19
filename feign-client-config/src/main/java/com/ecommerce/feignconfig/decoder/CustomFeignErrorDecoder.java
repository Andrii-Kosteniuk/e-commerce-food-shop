package com.ecommerce.feignconfig.decoder;

import com.ecommerce.commonexception.exception.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

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
            case 401 -> new UnauthorizedException(extractedMessage);
            case 403 -> new AccessRestrictedException(extractedMessage);
            case 404 -> new ResourceNotFoundException(extractedMessage);
            case 409 -> new ResourceAlreadyExistsException(extractedMessage);
            case 500 -> new CustomServerErrorException(extractedMessage);
            case 503 -> new ServiceUnavailableException(extractedMessage);
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

        } catch (Exception ignored) {
        }

        return body;
    }
}