package com.ecommerce.auth.controller;

import com.ecommerce.auth.jwt.JwtUtils;
import com.ecommerce.auth.jwt.RefreshTokenRequest;
import com.ecommerce.auth.model.AuthenticationRequest;
import com.ecommerce.auth.model.AuthenticationResponse;
import com.ecommerce.auth.model.RegisterRequest;
import com.ecommerce.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthController(AuthService authService, JwtUtils jwtUtils) {
        this.authService = authService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest registerRequest) {
        authService.registerUser(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        authService.authenticate(request);
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        if (jwtUtils.validateToken(request.refreshToken())) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        String userName = jwtUtils.getSubjectFromToken(request.refreshToken());
        String newAccessToken = jwtUtils.generateAccessTokenFromUserName(userName);

        return ResponseEntity.ok().body(new AuthenticationResponse(newAccessToken, request.refreshToken()));
    }
}
