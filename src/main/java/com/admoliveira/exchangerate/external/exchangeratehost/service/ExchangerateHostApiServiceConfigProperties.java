package com.admoliveira.exchangerate.external.exchangeratehost.service;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "exchange-rate.external.exchangerate-host-api-service")
public record ExchangerateHostApiServiceConfigProperties(int priority) {

}
