package com.admoliveira.exchangerate.external.exchangeratehost.service;

import com.admoliveira.exchangerate.external.ExchangeRateExternalApiService;
import com.admoliveira.exchangerate.external.exchangeratehost.client.ExchangerateHostClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@EnableConfigurationProperties(ExchangerateHostApiServiceConfigProperties.class)
public class ExchangerateHostApiService implements ExchangeRateExternalApiService {

    private final ExchangerateHostClient client;
    private final ExchangerateHostApiServiceConfigProperties configProperties;

    public ExchangerateHostApiService(final ExchangerateHostApiServiceConfigProperties configProperties,
                                      final ExchangerateHostClient client) {

        this.client = client;
        this.configProperties = configProperties;
    }

    @Override
    public Map<Currency, BigDecimal> getExchangeRates(final Currency currency) {
        return client.getLive(currency).quotes().entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(currency.getCurrencyCode()))
                .collect(Collectors.toMap(
                        entry -> Currency.getInstance(entry.getKey().substring(currency.getCurrencyCode().length())),
                        Map.Entry::getValue
                ));
    }

    @Override
    public int getPriority() {
        return configProperties.priority();
    }

}
