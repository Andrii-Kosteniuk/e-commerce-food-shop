package com.ecommerce.gateway.security;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
@RequiredArgsConstructor
public class ApiGatewayJwtAuthenticationFilter implements GlobalFilter {

    private final JwtUtil jwtUtil;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        if (path.startsWith("/api/v1/auth")) {
            return chain.filter(exchange);
        }

        String authHeader = exchange
                .getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return unauthorized(exchange);
        }

        String token = authHeader.substring(7);

        if (!jwtUtil.validateToken(token)) {
            return unauthorized(exchange);
        }

        Claims claims = jwtUtil.extractClaims(token);

        ServerHttpRequest mutatedRequest =
                exchange.getRequest()
                        .mutate()
                        .header("X-User-Email", claims.getSubject())
                        .header("X-User-Role",
                                claims.get("role", String.class))
                        .build();

        return chain.filter(
                exchange.mutate()
                        .request(mutatedRequest)
                        .build()
        );
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {

        exchange.getResponse()
                .setStatusCode(HttpStatus.UNAUTHORIZED);

        return exchange.getResponse().setComplete();
    }
}