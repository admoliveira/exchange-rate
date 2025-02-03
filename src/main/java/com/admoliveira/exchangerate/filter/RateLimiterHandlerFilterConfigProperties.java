package com.admoliveira.exchangerate.filter;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "exchange-rate.rate-limiter-handler-filter")
public record RateLimiterHandlerFilterConfigProperties(Duration window, int maxRequestsPerWindow) {

}
