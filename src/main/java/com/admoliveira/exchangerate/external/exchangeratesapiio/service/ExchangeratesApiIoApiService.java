package com.admoliveira.exchangerate.external.exchangeratesapiio.service;

import com.admoliveira.exchangerate.external.ExchangeRateExternalApiService;
import com.admoliveira.exchangerate.external.exchangeratesapiio.client.ExchangeratesApiIoClient;
import com.admoliveira.exchangerate.external.exchangeratesapiio.dto.ExchangeratesApiIoLatestResponse;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
@EnableConfigurationProperties(ExchangeratesApiIoApiServiceConfigProperties.class)
public class ExchangeratesApiIoApiService implements ExchangeRateExternalApiService {

    private final ExchangeratesApiIoClient client;
    private final ExchangeratesApiIoApiServiceConfigProperties configProperties;

    public ExchangeratesApiIoApiService(
            final ExchangeratesApiIoClient client,
            final ExchangeratesApiIoApiServiceConfigProperties configProperties) {
        this.client = client;
        this.configProperties = configProperties;
    }

    @Override
    public Map<String, BigDecimal> getExchangeRates(final String currency) {
        final ExchangeratesApiIoLatestResponse response = client.getLatest(currency);
        if (!response.success()) {
            throw new RuntimeException("Error getting exchange rates from ExchangeratesApiIo for currency: " + currency);
        }
        return response.rates();
    }

    @Override
    public int getPriority() {
        return configProperties.priority();
    }
}
