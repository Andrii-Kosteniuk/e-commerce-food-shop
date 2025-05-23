package com.ecommerce.user.service;

import com.commondto.auth.RegisterRequest;
import com.commonexception.exception.ResourceAlreadyExistsException;
import com.commonexception.exception.ResourceNotFoundException;

import com.ecommerce.user.model.User;
import com.ecommerce.user.repository.UserRepository;
import com.ecommerce.user.util.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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

        User user = userMapper.userRequestToUser(request);

        log.info("Creating new user in user-service:");
        user.setPassword(passwordEncoder.encode(request.password()));
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
    public User getRegisteredUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return (User) userDetails;
        }
        return null;
    }


}
