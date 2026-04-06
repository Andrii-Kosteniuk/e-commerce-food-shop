package com.ecommerce.commondto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RefreshTokenRequest(
        @NotNull(message = MESSAGE)
        @NotBlank(message = MESSAGE)
        String refreshToken) {

    private static final String MESSAGE = "Refresh token is required";
}