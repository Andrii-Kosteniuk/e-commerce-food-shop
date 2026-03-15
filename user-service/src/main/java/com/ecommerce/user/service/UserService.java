package com.ecommerce.user.service;

import com.ecommerce.commondto.auth.RegisterRequest;
import com.ecommerce.user.model.User;

public interface UserService {

    User createUser(RegisterRequest request);
    User getUserByEmail(String email);
}
