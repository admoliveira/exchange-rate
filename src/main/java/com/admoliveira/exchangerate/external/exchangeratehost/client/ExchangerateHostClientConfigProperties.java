package com.admoliveira.exchangerate.external.exchangeratehost.client;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "exchange-rate.external.exchangerate-host-client")
public record ExchangerateHostClientConfigProperties(String baseUrl, String apiKey) {

}
