package com.ecommerce.user.service;

import com.ecommerce.commondto.auth.RegisterRequest;
import com.ecommerce.commondto.user.UserResponse;
import com.ecommerce.user.model.User;

public interface UserService {

    User createUser(RegisterRequest request);
    User getUserById(Long id);
    User getUserByEmail(String email);
    UserResponse getRegisteredUser(String email);
}
