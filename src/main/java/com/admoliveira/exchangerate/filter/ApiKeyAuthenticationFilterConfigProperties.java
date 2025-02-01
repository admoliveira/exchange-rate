package com.admoliveira.exchangerate.filter;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

@ConfigurationProperties(prefix = "api-key-authentication")
public record ApiKeyAuthenticationFilterConfigProperties (Set<String> keys) {

}
