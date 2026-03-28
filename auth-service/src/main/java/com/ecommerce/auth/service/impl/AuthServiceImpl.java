package com.ecommerce.auth.service.impl;

import com.ecommerce.auth.jwt.JwtService;
import com.ecommerce.auth.service.AuthService;
import com.ecommerce.auth.feign.UserServiceClient;
import com.ecommerce.commondto.auth.AuthenticationRequest;
import com.ecommerce.commondto.auth.AuthenticationResponse;
import com.ecommerce.commondto.auth.RegisterRequest;
import com.ecommerce.commondto.user.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserServiceClient userServiceClient;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    @Override
    public AuthenticationResponse registerUser(RegisterRequest request) {

        log.info("Registering user in auth-service: {}", request.email());
        UserResponse user = userServiceClient.createUser(request);

        String token = jwtService.generateAccessToken(user.email(), user.role());

        return new AuthenticationResponse(token);
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        log.info("Authenticating user...");
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        UserResponse user = userServiceClient.getUserByEmail(request.email());
        String token = jwtService.generateAccessToken(user.email(), user.role());

        log.info("User '{}' has been authenticated", user.email());

        return new AuthenticationResponse(token);

    }
}
