package com.admoliveira.exchangerate.external.exchangeratesapiio.service;

import com.admoliveira.exchangerate.external.ExchangeRateExternalApiService;
import com.admoliveira.exchangerate.external.exchangeratesapiio.client.ExchangeratesApiIoClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

@Service
@EnableConfigurationProperties(ExchangeratesApiIoApiServiceConfigProperties.class)
public class ExchangeratesApiIoApiService implements ExchangeRateExternalApiService {

    private final ExchangeratesApiIoClient client;
    private final String apiKey;

    public ExchangeratesApiIoApiService(final ExchangeratesApiIoApiServiceConfigProperties configProperties) {
        final RestClient restClient = RestClient.builder()
                .baseUrl(configProperties.baseUrl())
                .build();
        final RestClientAdapter adapter = RestClientAdapter.create(restClient);
        final HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        this.client = factory.createClient(ExchangeratesApiIoClient.class);
        this.apiKey = configProperties.apiKey();
    }

    @Override
    public Map<Currency, BigDecimal> getExchangeRates(final Currency currency) {
        return client.getLatest(currency, apiKey).rates();
    }

    @Override
    public int getPriority() {
        return 1;
    }
}
