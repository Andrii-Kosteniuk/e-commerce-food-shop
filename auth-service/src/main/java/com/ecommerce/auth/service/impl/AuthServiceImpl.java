package com.ecommerce.auth.service.impl;

import com.ecommerce.auth.jwt.JwtUtils;
import com.ecommerce.auth.service.AuthService;
import com.ecommerce.auth.service.UserServiceClient;
import com.commondto.auth.AuthenticationRequest;
import com.commondto.auth.AuthenticationResponse;
import com.commondto.auth.RegisterRequest;
import com.commondto.user.UserResponse;
import com.ecommerce.user.model.User;
import com.ecommerce.user.util.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserServiceClient userServiceClient;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    @Override
    public void registerUser(RegisterRequest request) {
        userServiceClient.createNewUser(request);
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        var user = (UserDetails) getUserFromRequest(request.email());

        var accessToken = jwtUtils.generateAccessTokenFromUser(user);
        var refreshToken = jwtUtils.generateRefreshToken(request.email());

        return new AuthenticationResponse(accessToken, refreshToken);
    }

    @Override
    public User getUserFromRequest(String username) {
        log.info("Attempting to load user by username: {}", username);

        UserResponse userByEmail = userServiceClient.getUserForLogin(username);
        return userMapper.userResponceToUser(userByEmail);
    }
}
