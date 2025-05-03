package com.ecommerce.auth.security;

import com.ecommerce.auth.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityFilterChain {

    private final JwtAuthenticationFilter tokenFilter;

    public SecurityFilterChain(JwtAuthenticationFilter tokenFilter) {
        this.tokenFilter = tokenFilter;
    }

    @Bean
    public org.springframework.security.web.SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/localhost:8081/api/v1/items/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/localhost:8081/api/v1/items/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/localhost:8081/api/v1/items/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }

}
