package com.admoliveira.exchangerate.service;

import com.admoliveira.exchangerate.configuration.CacheConfig;
import com.admoliveira.exchangerate.external.ExchangeRateExternalApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Currency;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RatesService {

    private final List<ExchangeRateExternalApiService> externalApiServices;

    public RatesService(final List<ExchangeRateExternalApiService> externalApiServices) {
        this.externalApiServices = externalApiServices.stream()
                .sorted(Comparator.comparingInt(ExchangeRateExternalApiService::getPriority))
                .collect(Collectors.toList());
    }

    @Cacheable(value = CacheConfig.RATES_CACHE_NAME,  key = "#from.currencyCode")
    public Map<Currency, BigDecimal> getRates(final Currency from) {
        for (ExchangeRateExternalApiService externalApiService : externalApiServices) {
            try {
                return externalApiService.getExchangeRates(from);
            } catch (Exception e) {
                log.error("Error getting rates for currency {}", from, e);
            }
        }
        throw new RuntimeException("Unable to get rates for currency " + from);
    }

}
