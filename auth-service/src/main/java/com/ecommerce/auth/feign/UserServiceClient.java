package com.ecommerce.auth.feign;

import com.ecommerce.auth.feign.fallback.UserServiceClientFallbackFactory;
import com.ecommerce.commondto.auth.*;
import com.ecommerce.feignconfig.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "user-service",
        path = "/api/v1/internal/users",
        configuration = FeignClientConfig.class,
        fallbackFactory = UserServiceClientFallbackFactory.class)
public interface UserServiceClient {

    @PostMapping("/register")
    AuthenticationResponse registerUser(RegisterRequest request);

    @PostMapping("/login")
    AuthenticationResponse loginUser(AuthenticationRequest request);

    @PostMapping("/refresh-token")
    AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    @PostMapping("/logout")
    void logout(LogOutRequest request);
}
