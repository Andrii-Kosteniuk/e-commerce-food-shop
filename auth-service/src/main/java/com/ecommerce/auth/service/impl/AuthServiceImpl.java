package com.ecommerce.auth.service.impl;


import com.commonexception.exception.ResourceAlreadyExistsException;
import com.commonexception.exception.ResourceNotFoundException;
import com.ecommerce.auth.dto.AuthenticationRequest;
import com.ecommerce.auth.dto.AuthenticationResponse;
import com.ecommerce.auth.dto.RegisterRequest;
import com.ecommerce.auth.jwt.JwtUtils;
import com.ecommerce.auth.model.*;
import com.ecommerce.auth.repository.UserRepository;
import com.ecommerce.auth.service.AuthService;
import com.ecommerce.auth.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;

    }

    @Override
    public void registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.email())) {
            throw new ResourceAlreadyExistsException("User with such an email already exists");
        }

        User user = userMapper.registerRequestToUser(registerRequest);
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        userRepository.save(user);
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        var user = getUserFromRequest(request.email());

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials. Try to login again with correct login and password");
        }

        var accessToken = jwtUtils.generateAccessTokenFromUser(user);
        var refreshToken = jwtUtils.generateRefreshToken(request.email());

        return new AuthenticationResponse(accessToken, refreshToken);
    }

    @Override
    public User getUserFromRequest(String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with such an email does not exist"));
    }
}
