package com.ecommerce.auth.controller;

import com.ecommerce.auth.AuthMapper;
import com.ecommerce.auth.jwt.JwtUtils;
import com.ecommerce.auth.service.impl.AuthServiceImpl;
import com.commondto.auth.AuthenticationRequest;
import com.commondto.auth.AuthenticationResponse;
import com.commondto.auth.RefreshTokenRequest;
import com.commondto.auth.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authServiceImpl;
    private final JwtUtils jwtUtils;
    private final AuthMapper authMapper;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid RegisterRequest registerRequest) {
        authServiceImpl.registerUser(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid AuthenticationRequest request) {
        return ResponseEntity.ok(authServiceImpl.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        if (jwtUtils.validateToken(request.refreshToken())) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        String userName = jwtUtils.getSubjectFromToken(request.refreshToken());
        var user = authServiceImpl.getUserFromRequest(userName);
        String newAccessToken = jwtUtils.generateAccessTokenFromUser(authMapper.toUserDetails(user));

        return ResponseEntity.ok().body(new AuthenticationResponse(newAccessToken, request.refreshToken()));
    }
}
