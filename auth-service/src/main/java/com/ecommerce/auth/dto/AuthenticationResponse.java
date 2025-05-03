package com.ecommerce.auth.dto;

public record AuthenticationResponse(
        String token,
        String refreshToken
) {}