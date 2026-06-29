package com.ecommerce.user.service;

import com.ecommerce.commondto.auth.*;

public interface UserAuthenticationService {

    AuthenticationResponse registerUser(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);

    AuthenticationResponse refreshToken(RefreshTokenRequest tokenRequest);

    void logout(LogOutRequest request);
}
