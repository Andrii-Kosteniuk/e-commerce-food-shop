package com.ecommerce.notification;

import com.ecommerce.commondto.user.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name="user-service",
        path = "/api/v1/internal/users")
public interface UserServiceFeignClient {

    @GetMapping("/{id}")
    UserResponse getUserById(@PathVariable Long id);
}
