package com.ecommerce.user.service;

import com.ecommerce.security.token.TokenBlocklistKeys;
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

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${security.token-blocklist.prefix:#{null}}")
    private String blocklistPrefix;

    public void revoke(String tokenId, long expiryMillis) {
        redisTemplate.opsForValue()
                .set(blocklistKey(tokenId), "revoked", expiryMillis, TimeUnit.MILLISECONDS);

    }

    public Boolean isRevoked(String tokenId) {
        return redisTemplate.hasKey(blocklistKey(tokenId));
    }

    private String blocklistKey(String tokenId) {
        return TokenBlocklistKeys.key(blocklistPrefix, tokenId);
    }
}
