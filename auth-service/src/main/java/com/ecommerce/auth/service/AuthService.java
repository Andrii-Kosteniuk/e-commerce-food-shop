package com.ecommerce.auth.service;

import com.ecommerce.commondto.auth.AuthenticationRequest;
import com.ecommerce.commondto.auth.AuthenticationResponse;
import com.ecommerce.commondto.auth.RegisterRequest;

public interface AuthService {
    AuthenticationResponse registerUser(RegisterRequest registerRequest);
    AuthenticationResponse authenticate(AuthenticationRequest request);
}
