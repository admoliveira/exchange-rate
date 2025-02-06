package com.admoliveira.exchangerate.service;

import com.admoliveira.exchangerate.configuration.CacheConfig;
import com.admoliveira.exchangerate.model.RateLimitBucket;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import static org.apache.commons.lang3.time.DateUtils.MILLIS_PER_SECOND;

@Service
@Slf4j
@EnableConfigurationProperties(RateLimiterServiceConfigProperties.class)
public class RateLimiterService {

    private final RateLimitCached rateLimitCached;
    private final long windowSeconds;
    private final int maxRequestsPerWindow;

    public RateLimiterService(final RateLimitCached rateLimitCached,
                              final RateLimiterServiceConfigProperties configProperties) {
        this.windowSeconds = configProperties.window().toSeconds();
        this.maxRequestsPerWindow = configProperties.maxRequestsPerWindow();
        this.rateLimitCached = rateLimitCached;
    }

    public RateLimitBucket limit(final HttpServletRequest request) {
        final long slot = (System.currentTimeMillis() / MILLIS_PER_SECOND) / windowSeconds;
        final String key = String.format("%s_%s", request.getRemoteAddr(), slot);

        int count = rateLimitCached.getCount(key);
        int remaining = maxRequestsPerWindow - count;

        log.info("Rate limit for {}: {} ", key, count);
        if (remaining > 0) {
            remaining--;
            rateLimitCached.putCount(key, count + 1);
        }

        return new RateLimitBucket(maxRequestsPerWindow, remaining, (slot + 1) * windowSeconds);
    }

    @Component
    public static class RateLimitCached {
        @Cacheable(value = CacheConfig.RATE_LIMITER_CACHE_NAME, key = "#key")
        public int getCount(String key) {
            return 0;
        }

        @CachePut(value = CacheConfig.RATE_LIMITER_CACHE_NAME, key = "#key")
        public int putCount(String key, int count) {
            return count;
        }
    }

}
