package com.ecommerce.auth.service;

import com.ecommerce.commondto.auth.*;

public interface AuthService {
    AuthenticationResponse registerUser(RegisterRequest registerRequest);
    AuthenticationResponse authenticate(AuthenticationRequest request);
    AuthenticationResponse refreshToken(RefreshTokenRequest tokenRequest);
    void logout(LogOutRequest request);
}
