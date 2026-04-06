package com.ecommerce.commondto.auth;

import jakarta.validation.constraints.NotBlank;

public record LogOutRequest (
        @NotBlank(message = MESSAGE)
        String token,
        @NotBlank(message = MESSAGE)
        String refreshToken){

    private static final String MESSAGE = "Token is required";
}
