package com.ecommerce.user.security;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenBlocklistService {

    private final RedisTemplate<String, String > redisTemplate;

    public void revoke(String token, long expiryMillis) {
        redisTemplate.opsForValue()
                .set("blocklist:" + token, "revoked", expiryMillis, TimeUnit.MILLISECONDS);
    }

    public boolean isRevoked(String token) {
        return redisTemplate.hasKey("blocklist:" + token);
    }
}
