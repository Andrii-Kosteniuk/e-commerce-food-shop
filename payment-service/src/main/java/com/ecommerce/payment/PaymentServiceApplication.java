package com.ecommerce.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.ecommerce.payment",
        "com.ecommerce.commonexception",
        "com.ecommerce.commondto",
        "com.ecommerce.kafka",
        "com.ecommerce.security"
})
public class PaymentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceApplication.class, args);
    }

}
