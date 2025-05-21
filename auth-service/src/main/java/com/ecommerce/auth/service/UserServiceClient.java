package com.ecommerce.auth.service;

import com.ecommerce.auth.config.FeignClientConfig;
import com.commondto.auth.RegisterRequest;
import com.commondto.user.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", path = "/api/v1/users", configuration = FeignClientConfig.class)
public interface UserServiceClient {

    @PostMapping("/register-user")
    UserResponse createNewUser(@RequestBody RegisterRequest request);

    @GetMapping("/login-user")
    UserResponse getUserForLogin(@RequestParam("email") String email);

}
