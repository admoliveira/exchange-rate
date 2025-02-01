package com.admoliveira.exchangerate.external.exchangeratehost.service;

import com.admoliveira.exchangerate.external.ExchangeRateExternalApiService;
import com.admoliveira.exchangerate.external.exchangeratehost.client.ExchangerateHostClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@EnableConfigurationProperties(ExchangerateHostApiServiceConfigProperties.class)
public class ExchangerateHostApiService implements ExchangeRateExternalApiService {

    private final ExchangerateHostClient client;
    private final String apiKey;

    public ExchangerateHostApiService(final ExchangerateHostApiServiceConfigProperties configProperties) {
        final RestClient restClient = RestClient.builder()
                .baseUrl(configProperties.baseUrl())
                .build();
        final RestClientAdapter adapter = RestClientAdapter.create(restClient);
        final HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        this.client = factory.createClient(ExchangerateHostClient.class);
        this.apiKey = configProperties.apiKey();
    }

    @Override
    public Map<Currency, BigDecimal> getExchangeRates(final Currency currency) {
        return client.getLive(currency, apiKey).quotes().entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(currency.getCurrencyCode()))
                .collect(Collectors.toMap(
                        entry -> Currency.getInstance(entry.getKey().substring(currency.getCurrencyCode().length())),
                        Map.Entry::getValue
                ));
    }

    @Override
    public int getPriority() {
        return 0;
    }
}
