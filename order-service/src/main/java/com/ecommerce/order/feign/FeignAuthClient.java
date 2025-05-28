package com.ecommerce.order.feign;

import com.ecommerce.commondto.user.UserResponse;
import com.ecommerce.auth.feign.FeignClientAccessConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "auth-service", path = "/api/v1/auth", configuration = FeignClientAccessConfig.class)
public interface FeignAuthClient {

    @GetMapping("/authenticated-user")
    UserResponse getAuthenticatedUser();
}
