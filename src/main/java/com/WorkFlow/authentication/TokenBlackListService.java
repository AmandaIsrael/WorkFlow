package com.WorkFlow.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TokenBlackListService {
    private final StringRedisTemplate redisTemplate;

    public void addToBlacklist(String token, long expirationSeconds) {
        redisTemplate.opsForValue().set(token, "blacklisted", Duration.ofSeconds(expirationSeconds));
    }

    public boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(token));
    }
}
