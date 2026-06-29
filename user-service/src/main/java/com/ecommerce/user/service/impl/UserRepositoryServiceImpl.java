package com.ecommerce.user.service.impl;

import com.ecommerce.commondto.user.UserResponse;
import com.ecommerce.commonexception.exception.ResourceNotFoundException;
import com.ecommerce.user.mapper.UserMapper;
import com.ecommerce.user.model.User;
import com.ecommerce.user.repository.UserRepository;
import com.ecommerce.user.service.UserRepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRepositoryServiceImpl implements UserRepositoryService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Cacheable(value = "users", key = "#email")
    public UserResponse getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::userToUserResponse)
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

    }

    @Override
    @Cacheable(value = "users", key = "#userId")
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.userToUserResponse(user);
    }
}
