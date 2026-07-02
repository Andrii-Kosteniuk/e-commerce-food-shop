package com.ecommerce.auth;

import com.ecommerce.auth.feign.UserServiceClient;
import com.ecommerce.feignconfig.FeignClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {
        "com.ecommerce.auth",
        "com.ecommerce.commondto",
        "com.ecommerce.commonexception",
        "com.ecommerce.feignconfig"})
@EnableFeignClients(
        defaultConfiguration = FeignClientConfig.class,
        clients = UserServiceClient.class)
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

}
