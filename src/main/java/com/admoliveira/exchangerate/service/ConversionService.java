package com.admoliveira.exchangerate.service;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
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

    public Map<String, BigDecimal> getConversions(final String from, final Set<String> to, final BigDecimal amount) {
        Stream<Map.Entry<String, BigDecimal>> stream = ratesService.getRates(from).entrySet().stream();
        if (!CollectionUtils.isEmpty(to)) {
            stream = stream.filter(e -> to.contains(e.getKey()));
        }
        return stream.collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> amount.multiply(entry.getValue())
        ));
    }
}
