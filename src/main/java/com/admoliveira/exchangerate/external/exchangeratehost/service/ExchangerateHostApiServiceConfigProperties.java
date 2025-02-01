package com.admoliveira.exchangerate.external.exchangeratehost.service;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "external.exchangerate-host-api-service")
public record ExchangerateHostApiServiceConfigProperties(String baseUrl, String apiKey) {

}
