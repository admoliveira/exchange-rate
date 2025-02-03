package com.admoliveira.exchangerate.service;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "exchange-rate.rate-limiter-service")
public record RateLimiterServiceConfigProperties(Duration window, int maxRequestsPerWindow) {

}
