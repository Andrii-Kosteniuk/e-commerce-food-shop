package com.ecommerce.auth.service;

import com.ecommerce.auth.feign.FeignClientConfig;
import com.ecommerce.commondto.auth.RegisterRequest;
import com.ecommerce.commondto.user.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service", path = "/api/v1/users", configuration = FeignClientConfig.class)
public interface UserServiceClient {

    @PostMapping("/register-user")
    UserResponse createNewUser(@RequestBody RegisterRequest request);

    @GetMapping("/{email}")
    UserResponse getUserForLogin(@PathVariable("email") String email);

}
