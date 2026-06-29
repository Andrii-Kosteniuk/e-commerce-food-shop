package com.ecommerce.user.service.impl;

import com.ecommerce.commondto.auth.*;
import com.ecommerce.commondto.user.UserResponse;
import com.ecommerce.commonexception.exception.ResourceAlreadyExistsException;

import com.ecommerce.user.jwt.JwtService;
import com.ecommerce.user.mapper.UserMapper;
import com.ecommerce.user.model.Role;
import com.ecommerce.user.model.User;
import com.ecommerce.user.repository.UserRepository;
import com.ecommerce.user.security.TokenBlocklistService;
import com.ecommerce.user.service.UserAuthenticationService;
import com.ecommerce.user.service.UserRepositoryService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

    private final UserRepository userRepository;
    private final UserRepositoryService userRepositoryService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenBlocklistService tokenBlocklistService;
    private final JwtService jwtService;
    private final UserMapper userMapper;


    @Override
    @Transactional
    public AuthenticationResponse registerUser(RegisterRequest request) {
        String email = request.email();
        if (userRepository.existsByEmail(email)) {
            throw new ResourceAlreadyExistsException(
                    "User with such an email already exists"
            );
        }

        User user = new User();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.valueOf(request.role()));

        User savedUser = userRepository.save(user);

        UserResponse userResponse = userMapper.userToUserResponse(savedUser);

        String token = jwtService.generateAccessToken(userResponse.id(), userResponse.email(), userResponse.role());
        String refreshToken = jwtService.generateRefreshToken(userResponse.id(), userResponse.email(), userResponse.role());

        log.info("User '{}' has been registered with role {}", userResponse.email(), userResponse.role());

        return new AuthenticationResponse(token, refreshToken);
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        log.info("Authenticating user...");

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        UserResponse user = userRepositoryService.getUserByEmail(request.email());

        String accessToken = jwtService.generateAccessToken(user.id(), user.email(), user.role());
        String refreshToken = jwtService.generateRefreshToken(user.id(), user.email(), user.role());

        log.info("User '{}' has been authenticated", user.email());

        return new AuthenticationResponse(accessToken, refreshToken);

    }

    @Override
    public AuthenticationResponse refreshToken(RefreshTokenRequest tokenRequest) {
        var claims = jwtService.extractClaims(tokenRequest.refreshToken());
        var type = claims.get("typ", String.class);

        if (!"refresh".equals(type)) {

            log.error("Invalid refresh token");

            throw new JwtException("Not a refresh token");
        }

        var userId = claims.get("userId", Long.class);
        var role = claims.get("role", String.class);
        var email = claims.getSubject();

        String newAccessToken = jwtService.generateAccessToken(userId, email, role);
        String newRefreshToken = jwtService.generateRefreshToken(userId, email, role);

        return new AuthenticationResponse(newAccessToken, newRefreshToken);
    }

    @Override
    public void logout(LogOutRequest request) {
        revokeIfValid(request.token(), "access");
        revokeIfValid(request.refreshToken(), "refresh");
    }

    private void revokeIfValid(String token, String expectedType) {
        try {
            var extractedClaims = jwtService.extractClaims(token);

            var type = extractedClaims.get("typ", String.class);
            if (!expectedType.equals(type)) {
                throw new JwtException("Not a " + expectedType + " token");
            }

            long remaining = extractedClaims.getExpiration().getTime() - System.currentTimeMillis();

            if (remaining > 0) {
                String tokenId = extractedClaims.get("tokenId", String.class);
                tokenBlocklistService.revoke(tokenId, remaining);
            }
        } catch (JwtException e) {
            log.warn("Token invalid or expired during logout, skipping revocation: {}", e.getMessage());
        }
    }
}
