package com.ecommerce.gateway;

import com.ecommerce.gatewaysecurity.filter.GatewayAuthenticationFilter;
import com.ecommerce.gatewaysecurity.jwt.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TestGatewayAuthenticationFilter {

    @Mock
    JwtUtil jwtUtil;

    @Mock
    RedisTemplate<String, String> redisTemplate;

    @Mock
    WebFilterChain chain;

    @InjectMocks
    GatewayAuthenticationFilter authenticationFilter;

    static String TOKEN = """
            eyJhbGciOiJIUzI1NiJ9.
            eyJzdWIiOiJhbmRyaWkta29zdGVuaXVrQGdtYWlsLmNvbSIsInJvbGUiOiJBRE1
            JTiIsInVzZXJJZCI6MSwidG9rZW5JZCI6IjVMIn0.ApVxwzNgmpI_i4pGFvtckUkWPSqF8bBpHwkj6keW1Sg
            """;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authenticationFilter, "publicPath", "/api/v1/auth/");
        ReflectionTestUtils.setField(authenticationFilter, "internalPath", "/api/v1/internal/");
        ReflectionTestUtils.setField(authenticationFilter, "internalApiKey", "secret-key");

    }

    @Test
    void shouldSkipAuthenticationForPublicEndpoint() {
        // ARRANGE
        ServerWebExchange exchange =
                MockServerWebExchange.from(
                        MockServerHttpRequest.get("/api/v1/auth/login").build());

        when(chain.filter(any())).thenReturn(Mono.empty());

        // ACT
        authenticationFilter.filter(exchange, chain).block();

        // ASSERT
        verify(chain).filter(any());
    }

    @Test
    void shouldReturnForbiddenForInternalEndpoint() {
        // ARRANGE
        ServerWebExchange exchange =
                MockServerWebExchange.from(
                        MockServerHttpRequest.get("/api/v1/internal/test").build());

        // ACT
        authenticationFilter.filter(exchange, chain).block();

        // ASSERT
        assertEquals(HttpStatus.FORBIDDEN,
                exchange.getResponse().getStatusCode());

        verifyNoInteractions(jwtUtil);
    }

    @Test
    void shouldReturnUnauthorizedWhenHeaderMissing() {
        // ARRANGE
        ServerWebExchange exchange =
                MockServerWebExchange.from(
                        MockServerHttpRequest.get("/api/v1/orders").build());
        // ACT
        authenticationFilter.filter(exchange, chain).block();
        // ASSERT
        assertEquals(HttpStatus.UNAUTHORIZED,
                exchange.getResponse().getStatusCode());
    }

    @Test
    void testPathStartsWithPublicPath() {
        // ARRANGE
        ServerWebExchange exchange =
                MockServerWebExchange.from(
                        MockServerHttpRequest.get("/api/v1/auth/login").build());

        // ACT
        var actual = exchange.getRequest().getURI().getPath();

        // ASSERT
        assertThat(actual).startsWith("/api/v1/auth/");
    }

    @Test
    void testTokenIsInvalid() {
        // ARRANGE
        ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/v1/orders")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token123")
                        .build()
        );

        when(jwtUtil.isValidToken("token123")).thenReturn(false);

        // ACT
        authenticationFilter.filter(exchange, chain).block();

        // ASSERT
        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());

        verify(jwtUtil).isValidToken("token123");
        verifyNoMoreInteractions(jwtUtil);
        verifyNoInteractions(redisTemplate);
        verify(chain, never()).filter(any());
    }

    @Test
    void testDataAreExchangedSuccessfullyFromToken() {
        // ARRANGE
        ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/v1/orders")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN)
                        .build()
        );
        when(chain.filter(any(ServerWebExchange.class)))
                .thenReturn(Mono.empty());
        when(jwtUtil.isValidToken(TOKEN)).thenReturn(true);
        when(jwtUtil.extractTokenId(TOKEN)).thenReturn("5L");
        when(jwtUtil.extractUserId(TOKEN)).thenReturn(1L);
        when(jwtUtil.extractEmail(TOKEN)).thenReturn("andrii-kosteniuk@gmail.com");
        when(jwtUtil.extractRole(TOKEN)).thenReturn("ADMIN");

        // ACT
        authenticationFilter.filter(exchange, chain).block();

        // ASSERT
        verifyNoMoreInteractions(jwtUtil);
        verify(redisTemplate).hasKey("blocklist:" + "5L");

    }

    @Test
    void testTokenOnNullEmail() {
        // ARRANGE
        ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/v1/orders")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN)
                        .build()
        );

        when(jwtUtil.isValidToken(TOKEN)).thenReturn(true);
        when(jwtUtil.extractEmail(TOKEN)).thenReturn(null);
        when(jwtUtil.extractRole(TOKEN)).thenReturn(anyString());
        when(jwtUtil.extractUserId(TOKEN)).thenReturn(anyLong());

        // ACT
        authenticationFilter.filter(exchange, chain).block();

        // ASSERT
        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());

    }
    @Test
    void testTokenOnNullUserId() {
        // ARRANGE
        ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/v1/orders")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN)
                        .build()
        );

        when(jwtUtil.isValidToken(TOKEN)).thenReturn(true);
        when(jwtUtil.extractUserId(TOKEN)).thenReturn(null);
        when(jwtUtil.extractRole(TOKEN)).thenReturn(anyString());
        when(jwtUtil.extractEmail(TOKEN)).thenReturn(anyString());

        // ACT
        authenticationFilter.filter(exchange, chain).block();

        // ASSERT
        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());

    }

    @Test
    void testTokenOnNullRole() {
        // ARRANGE
        ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/v1/orders")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN)
                        .build()
        );

        when(jwtUtil.isValidToken(TOKEN)).thenReturn(true);
        when(jwtUtil.extractRole(TOKEN)).thenReturn(null);
        when(jwtUtil.extractEmail(TOKEN)).thenReturn(anyString());
        when(jwtUtil.extractUserId(TOKEN)).thenReturn(anyLong());


        // ACT
        authenticationFilter.filter(exchange, chain).block();

        // ASSERT
        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());

    }

    @Test
    void testTokenIsBlockListed() {
        // ARRANGE
        ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/v1/orders")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN)
                        .build()
        );

        when(jwtUtil.isValidToken(TOKEN)).thenReturn(true);
        when(jwtUtil.extractTokenId(TOKEN)).thenReturn("5L");
        when(redisTemplate.hasKey("blocklist:" + "5L")).thenReturn(Boolean.TRUE);

        // ACT
        authenticationFilter.filter(exchange, chain).block();

        // ASSERT
        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());

        verify(redisTemplate, times(1)).hasKey("blocklist:" + "5L");

    }

}
