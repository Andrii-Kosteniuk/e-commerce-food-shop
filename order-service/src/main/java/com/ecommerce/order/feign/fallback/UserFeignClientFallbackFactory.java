package com.ecommerce.order.feign.fallback;

import com.ecommerce.commondto.user.UserResponse;
import com.ecommerce.commonexception.exception.ServiceUnavailableException;
import com.ecommerce.order.feign.UserFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class UserFeignClientFallbackFactory implements FallbackFactory<UserFeignClient> {

    @Override
    public UserFeignClient create(Throwable cause) {
        return new UserFeignClient() {
            @Override
            public UserResponse getUserById(Long id) {
                log.error("[CircuitBreaker] user-service unavailable — getUserById({}): {}",
                        id, cause.getMessage());

                throw new ServiceUnavailableException(
                        "User service is currently unavailable. Please try again later.");

            }

            @Override
            public UserResponse getUserByEmail(String email) {
                log.error("[CircuitBreaker] user-service unavailable — getUserByEmail({}): {}",
                        email, cause.getMessage());

                throw new ServiceUnavailableException(
                        "User service is currently unavailable. Please try again later.");
            }
        };
    }
}
