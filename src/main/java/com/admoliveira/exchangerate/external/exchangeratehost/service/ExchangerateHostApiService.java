package com.admoliveira.exchangerate.external.exchangeratehost.service;

import com.admoliveira.exchangerate.external.ExchangeRateExternalApiService;
import com.admoliveira.exchangerate.external.exchangeratehost.client.ExchangerateHostClient;
import com.admoliveira.exchangerate.external.exchangeratehost.dto.ExchangerateHostLiveResponse;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    public Map<String, BigDecimal> getExchangeRates(final String currency) {
        final ExchangerateHostLiveResponse response = client.getLive(currency);
        if (!response.success()) {
            throw new RuntimeException("Error getting exchange rates from ExchangerateHost for currency: " + currency);
        }
        return response.quotes().entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(currency))
                .collect(Collectors.toMap(
                        entry -> entry.getKey().substring(currency.length()),
                        Map.Entry::getValue
                ));
    }

    @Override
    public int getPriority() {
        return configProperties.priority();
    }

}
