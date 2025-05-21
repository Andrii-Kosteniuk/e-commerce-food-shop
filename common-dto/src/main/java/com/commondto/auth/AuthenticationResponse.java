package com.commondto.auth;

public record AuthenticationResponse(
        String token,
        String refreshToken
) {}