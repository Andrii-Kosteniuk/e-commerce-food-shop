package com.ecommerce.auth.service.impl;

import com.commondto.user.UserResponse;
import com.ecommerce.auth.AuthMapper;
import com.ecommerce.auth.jwt.JwtUtils;
import com.ecommerce.auth.security.CustomUserDetails;
import com.ecommerce.auth.service.AuthService;
import com.ecommerce.auth.service.UserServiceClient;
import com.commondto.auth.AuthenticationRequest;
import com.commondto.auth.AuthenticationResponse;
import com.commondto.auth.RegisterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserServiceClient userServiceClient;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final AuthMapper authMapper;

    @Override
    public void registerUser(RegisterRequest request) {
        log.info("Registering user in auth-service: {}", request.email());
        userServiceClient.createNewUser(request);
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

        var user =  getUserFromRequest(request.email());
        log.info("User details =>  {}", user);

        var accessToken = jwtUtils.generateAccessTokenFromUser(authMapper.toUserDetails(user));
        var refreshToken = jwtUtils.generateRefreshToken(request.email());

        return new AuthenticationResponse(accessToken, refreshToken);
    }

    @Override
    public UserResponse getUserFromRequest(String username) {
        log.info("Attempting to load user by username: {}", username);

        return userServiceClient.getUserForLogin(username);
    }

    @Override
    public CustomUserDetails getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        return   principal instanceof CustomUserDetails ? (CustomUserDetails) principal : null;

    }
}
