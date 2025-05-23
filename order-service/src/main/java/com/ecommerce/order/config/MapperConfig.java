package com.ecommerce.order.config;

import com.ecommerce.user.util.UserMapper;
import com.ecommerce.user.util.UserMapperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
    @Bean
    public UserMapper userMapper() {
        return new UserMapperImpl();
    }
}
