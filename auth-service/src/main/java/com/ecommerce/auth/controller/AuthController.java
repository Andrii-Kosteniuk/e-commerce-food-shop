package com.ecommerce.auth.controller;

import com.ecommerce.commondto.user.UserResponse;
import com.ecommerce.auth.AuthMapper;
import com.ecommerce.auth.jwt.JwtUtils;
import com.ecommerce.auth.security.CustomUserDetails;
import com.ecommerce.auth.service.AuthService;
import com.ecommerce.auth.service.impl.AuthServiceImpl;
import com.ecommerce.commondto.auth.AuthenticationRequest;
import com.ecommerce.commondto.auth.AuthenticationResponse;
import com.ecommerce.commondto.auth.RefreshTokenRequest;
import com.ecommerce.commondto.auth.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authServiceImpl;
    private final JwtUtils jwtUtils;
    private final AuthMapper authMapper;
    private final AuthService authService;

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
        String newAccessToken = jwtUtils.generateAccessTokenFromUser(getUserDetails(user));

        return ResponseEntity.ok().body(new AuthenticationResponse(newAccessToken, request.refreshToken()));
    }

    @GetMapping("/user-details")
    public CustomUserDetails getUserDetails(@RequestBody UserResponse user) {
        return authMapper.toUserDetails(user);
    }


    @GetMapping("/authenticated-user")
    public UserResponse getAuthenticatedUser() {
        return authMapper.toUserResponse(authService.getAuthenticatedUser());
    }

}
