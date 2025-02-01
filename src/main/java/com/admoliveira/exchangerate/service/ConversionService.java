package com.admoliveira.exchangerate.service;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ConversionService {

    private final RatesService ratesService;

    public ConversionService(final RatesService ratesService) {
        this.ratesService = ratesService;
    }

    public Map<Currency, BigDecimal> getConversions(final Currency from, final Set<Currency> to, final BigDecimal amount) {
        Stream<Map.Entry<Currency, BigDecimal>> stream = ratesService.getRates(from).entrySet().stream();
        if (!CollectionUtils.isEmpty(to)) {
            stream = stream.filter(e -> to.contains(e.getKey()));
        }
        return stream.collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> amount.multiply(entry.getValue())
        ));
    }
}
