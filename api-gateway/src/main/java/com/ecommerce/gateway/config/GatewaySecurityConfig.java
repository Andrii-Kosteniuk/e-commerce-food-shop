package com.ecommerce.gateway.config;

import com.ecommerce.gatewaysecurity.filter.GatewayAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class GatewaySecurityConfig {

    private final GatewayAuthenticationFilter gatewayAuthenticationFilter;

    public GatewaySecurityConfig(GatewayAuthenticationFilter gatewayAuthenticationFilter) {
        this.gatewayAuthenticationFilter = gatewayAuthenticationFilter;
    }

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(
                                "/actuator/health",
                                "/actuator/info",
                                "/api/v1/auth/**"
                        ).permitAll()
                        .anyExchange().authenticated()
                )
                .addFilterBefore(gatewayAuthenticationFilter, SecurityWebFiltersOrder.AUTHORIZATION)
                .build();
    }
}