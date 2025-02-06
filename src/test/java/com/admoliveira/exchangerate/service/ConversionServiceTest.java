package com.admoliveira.exchangerate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.emptySet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConversionServiceTest {

    @Mock
    private RatesService ratesService;

    private ConversionService conversionService;

    private final Currency usd = Currency.getInstance("USD");
    private final Currency eur = Currency.getInstance("EUR");
    private final Currency gbp = Currency.getInstance("GBP");
    private final Map<Currency, BigDecimal> rates = Map.of(
            eur, BigDecimal.valueOf(0.85),
            gbp, BigDecimal.valueOf(0.75)
    );

    @BeforeEach
    void setUp() {
        conversionService = new ConversionService(ratesService);
    }

    @Test
    void successToNull() {
        when(ratesService.getRates(usd)).thenReturn(rates);

        final BigDecimal amount = BigDecimal.valueOf(100);
        final Map<Currency, BigDecimal> conversions = conversionService.getConversions(usd, null, amount);

        assertEquals(2, conversions.size());
        assertEquals(0, BigDecimal.valueOf(85).compareTo(conversions.get(eur)));
        assertEquals(0, BigDecimal.valueOf(75).compareTo(conversions.get(gbp)));
    }

    @Test
    void successToEmpty() {
        when(ratesService.getRates(usd)).thenReturn(rates);

        final BigDecimal amount = BigDecimal.valueOf(100);
        final Map<Currency, BigDecimal> conversions = conversionService.getConversions(usd, emptySet(), amount);

        assertEquals(2, conversions.size());
        assertEquals(0, BigDecimal.valueOf(85).compareTo(conversions.get(eur)));
        assertEquals(0, BigDecimal.valueOf(75).compareTo(conversions.get(gbp)));
    }

    @Test
    void successFilteredCurrencies() {
        when(ratesService.getRates(usd)).thenReturn(rates);

        BigDecimal amount = BigDecimal.valueOf(100);
        Set<Currency> toCurrencies = Set.of(eur);
        Map<Currency, BigDecimal> conversions = conversionService.getConversions(usd, toCurrencies, amount);

        assertEquals(1, conversions.size());
        assertEquals(0, BigDecimal.valueOf(85).compareTo(conversions.get(eur)));
    }
}
