package com.ecommerce.order.feign;


import com.ecommerce.commondto.user.UserResponse;
import com.ecommerce.feignconfig.FeignClientConfig;
import com.ecommerce.order.feign.fallback.UserFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.naming.ServiceUnavailableException;

@FeignClient(
        name = "user-service",
        path = "/api/v1/internal/users",
        fallbackFactory =  UserFeignClientFallbackFactory.class)
public interface UserFeignClient {

    @GetMapping("/{id}")
    UserResponse getUserById(@PathVariable Long id);


    @GetMapping("/email")
    UserResponse getUserByEmail(@RequestParam String email);
}
