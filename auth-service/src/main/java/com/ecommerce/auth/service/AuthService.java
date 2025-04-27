package com.ecommerce.auth.service;

import com.ecommerce.auth.model.RegisterRequest;
import com.ecommerce.auth.model.User;
import com.ecommerce.auth.model.UserMapper;
import com.ecommerce.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.email())) {
            throw new IllegalArgumentException("User with such an email already exists");
        }

        User user = userMapper.registerRequestToUser(registerRequest);
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        userRepository.save(user);
    }
}
