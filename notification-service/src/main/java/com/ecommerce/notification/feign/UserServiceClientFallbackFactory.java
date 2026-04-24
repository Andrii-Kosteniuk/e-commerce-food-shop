package com.ecommerce.notification.feign;

import com.ecommerce.commondto.user.UserResponse;
import com.ecommerce.commonexception.exception.ServiceUnavailableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserServiceClientFallbackFactory implements FallbackFactory<UserServiceFeignClient> {
    @Override
    public UserServiceFeignClient create(Throwable cause) {
        return new UserServiceFeignClient() {
            @Override
            public UserResponse getUserById(Long id) {
                log.error("[CircuitBreaker] user-service unavailable — getUserById({}): {}",
                        id, cause.getMessage());

                throw new ServiceUnavailableException(
                        "User service is currently unavailable. Please try again later.");
            }
        };
    }
}
