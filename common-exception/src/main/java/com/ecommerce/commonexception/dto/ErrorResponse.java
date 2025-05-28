package com.ecommerce.commonexception.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record ErrorResponse(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        LocalDateTime timestamp,

        @JsonProperty(value = "status")
        int status,

        @JsonProperty(value = "reason")
        String error,

        @JsonProperty(value = "details")
        String message,

        @JsonProperty(value = "path")
        String path
) {
}
