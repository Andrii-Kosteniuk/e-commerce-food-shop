package com.ecommerce.auth.service.impl;

import com.ecommerce.auth.service.AuthService;
import com.ecommerce.auth.feign.UserServiceClient;
import com.ecommerce.commondto.auth.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserServiceClient userServiceClient;

    @Override
    public AuthenticationResponse registerUser(@Valid @RequestBody RegisterRequest request) {
        log.info("Registering user in auth-service: {}", request.email());
        return userServiceClient.registerUser(request);
    }

    @Override
    public AuthenticationResponse authenticate(@Valid @RequestBody AuthenticationRequest request) {
        return userServiceClient.loginUser(request);

    }

    @Override
    public AuthenticationResponse refreshToken(@Valid @RequestBody RefreshTokenRequest tokenRequest) {
        return userServiceClient.refreshToken(tokenRequest);
    }

    @Override
    public void logout(@Valid @RequestBody LogOutRequest request) {
        userServiceClient.logout(request);
    }
}
