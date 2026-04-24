package com.ecommerce.user.service;

import com.ecommerce.commondto.auth.*;
import com.ecommerce.commondto.user.UserResponse;
import com.ecommerce.user.model.User;

public interface UserService {

    AuthenticationResponse registerUser(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);

    AuthenticationResponse refreshToken(RefreshTokenRequest tokenRequest);

    void logout(LogOutRequest request);

    UserResponse getUserByEmail(String email);

    UserResponse getUserById(Long userId);
}
