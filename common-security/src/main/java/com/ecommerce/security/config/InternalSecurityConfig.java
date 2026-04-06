package com.ecommerce.security.config;

import com.ecommerce.security.filter.InternalAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InternalSecurityConfig {

    @Bean
    public InternalAuthenticationFilter internalAuthenticationFilter() {
        return new InternalAuthenticationFilter();
    }
}
