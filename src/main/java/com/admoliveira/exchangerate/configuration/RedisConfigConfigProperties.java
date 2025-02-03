package com.admoliveira.exchangerate.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "exchange-rate.redis-config")
public record RedisConfigConfigProperties(RatesConfig rates, RateLimiterConfig rateLimiter) {
    public record RatesConfig(Duration timeToLive) {}
    public record RateLimiterConfig(Duration timeToLive) {}
}
