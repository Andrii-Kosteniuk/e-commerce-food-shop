package com.ecommerce.auth.service;

import com.ecommerce.auth.dto.AuthenticationRequest;
import com.ecommerce.auth.dto.AuthenticationResponse;
import com.ecommerce.auth.dto.RegisterRequest;
import com.ecommerce.auth.model.User;

public interface AuthService {
    void registerUser(RegisterRequest registerRequest);
    AuthenticationResponse authenticate(AuthenticationRequest request);
    User getUserFromRequest(String username);
}
