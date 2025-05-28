package com.ecommerce.order;

import com.ecommerce.order.feign.FeignAuthClient;
import com.ecommerce.order.feign.FeignItemClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.ecommerce.order", "com.ecommerce.commonexception", "com.ecommerce.commondto", "com.ecommerce.auth"})
@EnableFeignClients(clients = {FeignAuthClient.class, FeignItemClient.class})
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

}
