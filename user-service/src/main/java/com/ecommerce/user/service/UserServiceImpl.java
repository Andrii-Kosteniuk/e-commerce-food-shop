package com.ecommerce.user.service;

import com.ecommerce.commondto.auth.RegisterRequest;
import com.ecommerce.commondto.user.UserResponse;
import com.ecommerce.commonexception.exception.ResourceAlreadyExistsException;
import com.ecommerce.commonexception.exception.ResourceNotFoundException;

import com.ecommerce.user.model.User;
import com.ecommerce.user.model.UserMapper;
import com.ecommerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public User createUser(RegisterRequest request) {
        User userByEmail = getUserByEmail(request.email());

        if (userByEmail != null) {
            log.error("User with email {} already exists", request.email());
            throw new ResourceAlreadyExistsException("User with such an email already exists");
        }

        User user = new User();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(request.role());

        log.info("Creating new user in user-service:");
        return userRepository.save(user);
    }


    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public User getUserByEmail(String email) {
        log.info("Retrieving user from user-service by email: {}", email);
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public UserResponse getRegisteredUser(String email) {

        log.info("Fetching user with email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        log.info("User found: {}", user);

        return userMapper.userToUserResponse(user);
    }

}
