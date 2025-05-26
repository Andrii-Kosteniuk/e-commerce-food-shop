package com.ecommerce.order.service;

import com.commondto.user.UserResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;


@FeignClient(name = "user-service", path = "/api/v1/users")
public interface UserServiceClient {

    @PostMapping("/authenticated-user")
    UserResponse getAuthenticatedUser();


}
