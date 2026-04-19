package com.ecommerce.auth.feign.fallback;

import com.ecommerce.auth.feign.UserServiceClient;
import com.ecommerce.commondto.auth.*;
import com.ecommerce.commonexception.exception.ServiceUnavailableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserServiceClientFallbackFactory implements FallbackFactory<UserServiceClient> {

    @Override
    public UserServiceClient create(Throwable cause) {
        return new UserServiceClient() {

            @Override
            public AuthenticationResponse registerUser(RegisterRequest request) {
                log.error("[CircuitBreaker] user-service unavailable — registerUser: {}",
                        cause.getMessage());
                throw new ServiceUnavailableException(
                        "Registration is temporarily unavailable. Please try again later.");
            }

            @Override
            public AuthenticationResponse loginUser(AuthenticationRequest request) {
                log.error("[CircuitBreaker] user-service unavailable — loginUser: {}",
                        cause.getMessage());
                throw new ServiceUnavailableException(
                        "Login is temporarily unavailable. Please try again later.");
            }

            @Override
            public AuthenticationResponse refreshToken(RefreshTokenRequest request) {
                log.error("[CircuitBreaker] user-service unavailable — refreshToken: {}",
                        cause.getMessage());
                throw new ServiceUnavailableException(
                        "Token refresh is temporarily unavailable. Please try again later.");
            }

            @Override
            public void logout(LogOutRequest request) {
                log.warn("[CircuitBreaker] user-service unavailable — logout skipped. "
                        + "Token will expire via TTL. Cause: {}", cause.getMessage());
            }
        };
    }
}
