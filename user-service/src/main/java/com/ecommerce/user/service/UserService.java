package com.ecommerce.user.service;

import com.commondto.auth.RegisterRequest;
import com.ecommerce.user.model.Address;
import com.ecommerce.user.model.User;

public interface UserService {

    User createUser(RegisterRequest request);
    User getUserById(Long id);
    User getUserByEmail(String email);
    Address getUserAddress(Long userId);

    User getRegisteredUser(String email);
}
