package com.ecommerce.auth.service;

import com.commondto.auth.AuthenticationRequest;
import com.commondto.auth.AuthenticationResponse;

import com.commondto.auth.RegisterRequest;
import com.commondto.user.UserResponse;

public interface AuthService {
    void registerUser(RegisterRequest registerRequest);
    AuthenticationResponse authenticate(AuthenticationRequest request);
    UserResponse getUserFromRequest(String username);
}
