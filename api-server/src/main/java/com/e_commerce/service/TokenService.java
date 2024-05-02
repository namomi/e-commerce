package com.e_commerce.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final StringRedisTemplate redisTemplate;

    public void markTokenActive(String token) {
        redisTemplate.opsForValue().set(token, "active");
    }

    public void markTokenInactive(String token) {
        redisTemplate.opsForValue().set(token, "inactive");
    }

    public boolean isTokenActive(String token) {
        String status = redisTemplate.opsForValue().get(token);
        return "active".equals(status);
    }
}
