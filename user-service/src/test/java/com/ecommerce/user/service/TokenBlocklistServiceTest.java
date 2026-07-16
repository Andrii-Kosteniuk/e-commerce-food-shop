package com.ecommerce.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = {
        "spring.cloud.config.enabled=false",
        "security.internal-api-key=some-internal-api-key"})
class TokenBlocklistServiceTest {

    private static final String PREFIX = "blocklist";

    @Mock
    RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    TokenBlocklistService tokenBlocklistService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(tokenBlocklistService, "blocklistPrefix", null);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void shouldReadTokenFromRedisAndDefineWhetherTokenIsRevoked() {
        // ARRANGE
        when(redisTemplate.hasKey(PREFIX + ":token123")).thenReturn(true);

        // ACT
        // ASSERT
        assertTrue(tokenBlocklistService.isRevoked("token123"));
        verify(redisTemplate).hasKey(PREFIX + ":token123");
    }

    @Test
    void shouldDoRevokeTokenDirectly() {
        // ARRANGE
        String tokenId = "token123";
        long expiry = 5000L;

        // ACT
        tokenBlocklistService.revoke(tokenId, expiry);

        // ASSERT
        verify(valueOperations).set(
                PREFIX + ":token123",
                "revoked",
                expiry,
                TimeUnit.MILLISECONDS
        );
    }


}
