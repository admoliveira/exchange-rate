package com.admoliveira.exchangerate.service;

import com.admoliveira.exchangerate.configuration.RedisCacheConfig;
import com.admoliveira.exchangerate.exception.JwtSubjectNotFound;
import com.admoliveira.exchangerate.model.RateLimitStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.time.DateUtils.MILLIS_PER_SECOND;

@Service
@Slf4j
@EnableConfigurationProperties(RateLimiterServiceConfigProperties.class)
public class RateLimiterService {

    private final RedisTemplate<String, Long> redisTemplate;
    private final long windowSeconds;
    private final long maxRequestsPerWindow;

    public RateLimiterService(final RedisTemplate<String, Long> redisTemplate,
                              final RateLimiterServiceConfigProperties configProperties) {
        this.redisTemplate = redisTemplate;
        this.windowSeconds = configProperties.window().toSeconds();
        this.maxRequestsPerWindow = configProperties.maxRequestsPerWindow();
    }

    public RateLimitStatus limit() {
        final long slot = (System.currentTimeMillis() / MILLIS_PER_SECOND) / windowSeconds;
        final String key = String.format("%s::%s_%s", RedisCacheConfig.RATE_LIMITER_CACHE_NAME, getJwtSubject(), slot);

        long count = incrementRedis(key);
        long remaining = maxRequestsPerWindow - count;

        boolean allowed = false;
        if (remaining >= 0) {
            allowed = true;
        } else {
            remaining = 0;
        }
        return new RateLimitStatus(maxRequestsPerWindow, remaining, (slot + 1) * windowSeconds, allowed);
    }

    private String getJwtSubject() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject();
        }
        throw new JwtSubjectNotFound();
    }

    public long incrementRedis(final String key) {
        return redisTemplate.execute(new SessionCallback<>() {
            @Override
            public Long execute(@NonNull final RedisOperations operations) throws DataAccessException {
                operations.multi();
                operations.opsForValue().increment(key);
                operations.expire(key, windowSeconds, TimeUnit.SECONDS);
                return (Long) operations.exec().getFirst();
            }
        });
    }

}
