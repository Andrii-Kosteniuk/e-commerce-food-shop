package com.ecommerce.order.feign;

import com.commondto.user.UserResponse;
import com.ecommerce.auth.feign.FeignClientAccessConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "auth-service", path = "/api/v1/auth", configuration = FeignClientAccessConfig.class)
public interface FeignAuthClient {

    @GetMapping("/user-details")
    UserDetails getUserDetails(@RequestBody UserResponse user);

    @GetMapping("/authenticated-user")
    UserResponse getAuthenticatedUser();
}
