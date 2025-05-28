package com.ecommerce.auth.service;

import com.ecommerce.commondto.auth.AuthenticationRequest;
import com.ecommerce.commondto.auth.AuthenticationResponse;

import com.ecommerce.commondto.auth.RegisterRequest;
import com.ecommerce.commondto.user.UserResponse;
import com.ecommerce.auth.security.CustomUserDetails;

public interface AuthService {
    void registerUser(RegisterRequest registerRequest);
    AuthenticationResponse authenticate(AuthenticationRequest request);
    UserResponse getUserFromRequest(String username);
    CustomUserDetails getAuthenticatedUser();
}
