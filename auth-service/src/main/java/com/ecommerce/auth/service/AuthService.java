package com.ecommerce.auth.service;

import com.commondto.auth.AuthenticationRequest;
import com.commondto.auth.AuthenticationResponse;

import com.commondto.auth.RegisterRequest;
import com.ecommerce.user.model.User;


public interface AuthService {
    void registerUser(RegisterRequest registerRequest);
    AuthenticationResponse authenticate(AuthenticationRequest request);
    User getUserFromRequest(String username);
}
