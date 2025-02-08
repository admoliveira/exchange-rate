package com.admoliveira.exchangerate.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "exchange-rate.cache")
public record CacheConfigProperties(RatesConfig rates, RateLimiterConfig rateLimiter) {
    public record RatesConfig(Duration timeToLive) {
    }

    public record RateLimiterConfig(Duration timeToLive) {
    }
}
