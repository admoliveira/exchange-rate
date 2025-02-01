package com.admoliveira.exchangerate.service;

import com.admoliveira.exchangerate.external.ExchangeRateExternalApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    public Map<Currency, BigDecimal> getRates(final Currency from) {
        return getRates(from, Collections.emptySet());
    }

    public Map<Currency, BigDecimal> getRates(final Currency from, final Set<Currency> to) {
        for (ExchangeRateExternalApiService externalApiService : externalApiServices) {
            try {
                Map<Currency, BigDecimal> rates = externalApiService.getExchangeRates(from);
                return CollectionUtils.isEmpty(to) ? rates : filterRates(rates, to);
            } catch (Exception e) {
                log.error("Error getting rates for currency {}", from, e);
            }
        }
        throw new RuntimeException("Unable to get rates for currency " + from);
    }

    private static Map<Currency, BigDecimal> filterRates(final Map<Currency, BigDecimal> rates, final Set<Currency> to) {
        return rates.entrySet().stream()
                .filter(entry -> to.contains(entry.getKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue)
                );
    }
}
