package com.ecommerce.commondto.auth;

public record AuthenticationResponse(
        String token,
        String refreshToken
) {}