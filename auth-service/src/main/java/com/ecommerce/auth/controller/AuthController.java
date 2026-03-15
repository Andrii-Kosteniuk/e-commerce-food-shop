package com.ecommerce.auth.controller;

import com.ecommerce.auth.service.AuthService;
import com.ecommerce.commondto.auth.AuthenticationRequest;
import com.ecommerce.commondto.auth.AuthenticationResponse;
import com.ecommerce.commondto.auth.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerUser(@Valid  @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.registerUser(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody  AuthenticationRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
