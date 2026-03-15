package com.ecommerce.auth.service;

import com.ecommerce.auth.feign.FeignClientConfig;
import com.ecommerce.commondto.auth.RegisterRequest;
import com.ecommerce.commondto.user.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service", path = "/api/v1/internal/users", configuration = FeignClientConfig.class)
public interface UserServiceClient {

    @PostMapping("/create")
    UserResponse createUser(@RequestBody RegisterRequest request);

    @GetMapping("/email")
    UserResponse getUserByEmail(@RequestParam String email);

}
