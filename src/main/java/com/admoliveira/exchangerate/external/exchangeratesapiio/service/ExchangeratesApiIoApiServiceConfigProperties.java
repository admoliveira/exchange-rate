package com.admoliveira.exchangerate.external.exchangeratesapiio.service;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "exchange-rate.external.exchangerates-api-io-api-service")
public record ExchangeratesApiIoApiServiceConfigProperties(String baseUrl, String apiKey, int priority) {

}
