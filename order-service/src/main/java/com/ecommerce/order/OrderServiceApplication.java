package com.ecommerce.order;

import com.ecommerce.feignconfig.FeignClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.ecommerce.order","com.ecommerce.commonexception", "com.ecommerce.commondto", "com.ecommerce.kafka", "com.ecommerce.security"})
@EnableFeignClients(
        basePackages = "com.ecommerce.order.feign",
        defaultConfiguration = FeignClientConfig.class)
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

}
