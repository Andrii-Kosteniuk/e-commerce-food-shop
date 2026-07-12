package com.ecommerce.gateway;

import com.ecommerce.gateway.config.RateLimiterConfig;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;

import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;

import java.net.InetSocketAddress;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestRateLimiterConfig {

    private final RateLimiterConfig config = new RateLimiterConfig();

    @Test
    void shouldReturnClientIpAddress() {

        KeyResolver keyResolver = config.ipKeyResolver();

        MockServerHttpRequest request = MockServerHttpRequest.get("/test")
                .remoteAddress(new InetSocketAddress("192.168.1.100", 8080))
                .build();

        ServerWebExchange exchange = MockServerWebExchange.from(request);

        String key = keyResolver.resolve(exchange).block();

        assertEquals("192.168.1.100", key);
    }

    @Test
    void shouldReturnAnonymousWhenRemoteAddressIsMissing() {

        KeyResolver keyResolver = config.ipKeyResolver();

        MockServerHttpRequest request = MockServerHttpRequest.get("/test")
                .build();

        ServerWebExchange exchange = MockServerWebExchange.from(request);

        String key = keyResolver.resolve(exchange).block();

        assertEquals("anonymous", key);
    }
}
