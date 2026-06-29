package com.ecommerce.auth.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@RequiredArgsConstructor
public class AuthFilterChain {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(this::disableCsrfForStatelessApi)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/auth/**").permitAll()
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
