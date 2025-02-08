package com.admoliveira.exchangerate.external.exchangeratesapiio.client;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "exchange-rate.external.exchangerates-api-io-client")
public record ExchangeratesApiIoClientConfigProperties(String baseUrl, String apiKey) {

}
