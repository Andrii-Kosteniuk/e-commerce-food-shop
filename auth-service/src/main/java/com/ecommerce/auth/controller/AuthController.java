package com.ecommerce.auth.controller;

import com.ecommerce.auth.jwt.JwtUtils;
import com.ecommerce.auth.dto.RefreshTokenRequest;
import com.ecommerce.auth.dto.AuthenticationRequest;
import com.ecommerce.auth.dto.AuthenticationResponse;
import com.ecommerce.auth.dto.RegisterRequest;
import com.ecommerce.auth.service.impl.AuthServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthServiceImpl authServiceImpl;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthController(AuthServiceImpl authServiceImpl, JwtUtils jwtUtils) {
        this.authServiceImpl = authServiceImpl;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid RegisterRequest registerRequest) {
        authServiceImpl.registerUser(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid AuthenticationRequest request) {
        authServiceImpl.authenticate(request);
        return ResponseEntity.ok(authServiceImpl.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        if (jwtUtils.validateToken(request.refreshToken())) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        String userName = jwtUtils.getSubjectFromToken(request.refreshToken());
        String newAccessToken = jwtUtils.generateAccessTokenFromUser(authServiceImpl.getUserFromRequest(userName));

        return ResponseEntity.ok().body(new AuthenticationResponse(newAccessToken, request.refreshToken()));
    }
}
