package com.ecommerce.auth.config;

import com.ecommerce.user.util.UserMapper;
import com.ecommerce.user.util.UserMapperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public UserMapper userMapper() {
        return new UserMapperImpl();
    }

}
