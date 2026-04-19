package com.ecommerce.user.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenBlocklistService {

    private static final String DEFAULT_PREFIX = "blocklist";

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${security.token-blocklist.prefix:#{null}}")
    private String prefix;

    public void revoke(String tokenId, long expiryMillis) {
        redisTemplate.opsForValue()
                .set(blocklistKey(tokenId), "revoked", expiryMillis, TimeUnit.MILLISECONDS);

    }

    public boolean isRevoked(String tokenId) {
        return redisTemplate.hasKey(blocklistKey(tokenId));
    }

    private String blocklistKey(String tokenId) {
        return resolvePrefix() + ":" + tokenId;
    }

    private String resolvePrefix() {
        return (prefix != null && !prefix.isBlank()) ? prefix : DEFAULT_PREFIX;
    }
}
