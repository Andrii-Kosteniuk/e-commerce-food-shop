package com.ecommerce.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@SpringBootApplication(scanBasePackages = {"com.ecommerce.product", "com.ecommerce.commonexception", "com.ecommerce.commondto", "com.ecommerce.security", "com.ecommerce.kafka"})
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class ProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }

}