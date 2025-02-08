package com.admoliveira.exchangerate.external.exchangeratesapiio.service;

import com.admoliveira.exchangerate.external.ExchangeRateExternalApiService;
import com.admoliveira.exchangerate.external.exchangeratesapiio.client.ExchangeratesApiIoClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Currency;
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
    public Map<Currency, BigDecimal> getExchangeRates(final Currency currency) {
        return client.getLatest(currency).rates();
    }

    @Override
    public int getPriority() {
        return configProperties.priority();
    }
}
