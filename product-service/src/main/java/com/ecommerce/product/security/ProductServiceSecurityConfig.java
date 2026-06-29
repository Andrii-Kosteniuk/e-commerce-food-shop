package com.ecommerce.product.security;

import com.ecommerce.security.filter.InternalAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ProductServiceSecurityConfig {

    private final InternalAuthenticationFilter internalAuthenticationFilter;

    @Bean
    public SecurityFilterChain serviceSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(this::disableCsrfForStatelessApi)
                .addFilterBefore(internalAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/admin/products/**").hasAuthority("ADMIN")
                        .anyRequest().authenticated())
                .build();
    }

    /**
     * CSRF protection is not required for this service.
     * All endpoints are stateless: authentication is performed via Bearer tokens
     * or the internal API key header (X-Internal-API-Key).
     */
    @SuppressWarnings("java:S4502")
    private void disableCsrfForStatelessApi(CsrfConfigurer<HttpSecurity> csrf) {
        csrf.disable();
    }
}
