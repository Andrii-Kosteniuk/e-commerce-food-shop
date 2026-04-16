package com.ecommerce.user.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenBlocklistService {

    private final RedisTemplate<String, String > redisTemplate;

    public void revoke(String tokenId, long expiryMillis) {
        redisTemplate.opsForValue()
                .set("blocklist:" + tokenId, "revoked", expiryMillis, TimeUnit.MILLISECONDS);

    }

    public boolean isRevoked(String token) {
        return redisTemplate.hasKey("blocklist:" + token);
    }
}
