package com.ecommerce.user.service;

import com.ecommerce.commondto.user.UserResponse;

public interface UserRepositoryService {
    UserResponse getUserByEmail(String email);
    UserResponse getUserById(Long userId);
}
