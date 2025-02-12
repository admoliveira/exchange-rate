package com.admoliveira.exchangerate.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "exchange-rate.redis-cache")
public record RedisCacheConfigProperties(RatesConfig rates) {
    public record RatesConfig(Duration timeToLive) {
    }
}
