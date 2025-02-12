package com.admoliveira.exchangerate.service;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "exchange-rate.rate-limiter-service")
public record RateLimiterServiceConfigProperties(Duration window, long maxRequestsPerWindow) {

    public RateLimiterServiceConfigProperties {
        if (window.toSeconds() < 1) {
            throw new IllegalArgumentException("Window duration must be at least 1 second.");
        }
    }

}
