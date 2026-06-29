package com.ecommerce.auth.feign.fallback;

import com.ecommerce.auth.feign.UserServiceClient;
import com.ecommerce.commondto.auth.*;
import com.ecommerce.commonexception.exception.ResourceAlreadyExistsException;
import com.ecommerce.commonexception.exception.ResourceNotFoundException;
import com.ecommerce.commonexception.exception.ServiceUnavailableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserServiceClientFallbackFactory implements FallbackFactory<UserServiceClient> {

    @Override
    public UserServiceClient create(Throwable cause) {
        return new UserServiceClient() {

            @Override
            public AuthenticationResponse registerUser(RegisterRequest request) {
                log.error("[CircuitBreaker] user-service temporary unavailable — registerUser: {}", cause.getMessage());

                throw rethrowIfBusinessError(cause, "Registration is temporarily unavailable. Please try again later.");
            }

            @Override
            public AuthenticationResponse loginUser(AuthenticationRequest request) {
                log.error("[CircuitBreaker] user-service temporary unavailable — loginUser: {}", cause.getMessage());

                throw rethrowIfBusinessError(cause, "Login is temporarily unavailable. Please try again later.");
            }

            @Override
            public AuthenticationResponse refreshToken(RefreshTokenRequest request) {
                log.error("[CircuitBreaker] user-service temporary unavailable — refreshToken: {}", cause.getMessage());
                throw rethrowIfBusinessError(cause, "Token refresh is temporarily unavailable. Please try again later.");
            }

            @Override
            public void logout(LogOutRequest request) {
                log.warn("[CircuitBreaker] user-service temporary unavailable — logout skipped. "
                        + "Token will expire via TTL. Cause: {}", cause.getMessage());
            }
        };
    }

    private RuntimeException rethrowIfBusinessError(Throwable cause, String fallbackMessage) {
        Throwable current = cause;
        while (current != null) {
            if (current instanceof ResourceAlreadyExistsException alreadyExists) {
                return alreadyExists;
            }
            if (current instanceof BadCredentialsException unauthorized) {
                return unauthorized;
            }
            if (current instanceof AccessDeniedException accessRestricted) {
                return accessRestricted;
            }
            if (current instanceof ResourceNotFoundException notFound) {
                return notFound;
            }
            current = current.getCause();
        }
        return new ServiceUnavailableException(fallbackMessage);
    }
}
