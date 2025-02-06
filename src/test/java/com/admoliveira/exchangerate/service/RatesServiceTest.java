package com.admoliveira.exchangerate.service;

import com.admoliveira.exchangerate.external.ExchangeRateExternalApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RatesServiceTest {

    @Mock
    private ExchangeRateExternalApiService apiService1;

    @Mock
    private ExchangeRateExternalApiService apiService2;

    private RatesService ratesService;

    @BeforeEach
    void setUp() {
        when(apiService1.getPriority()).thenReturn(1);
        when(apiService2.getPriority()).thenReturn(2);
        ratesService = new RatesService(List.of(apiService1, apiService2));
    }

    @Test
    void success() {
        final Currency usd = Currency.getInstance("USD");
        final Map<Currency, BigDecimal> expectedRates = Map.of(
                Currency.getInstance("EUR"), BigDecimal.valueOf(0.85),
                Currency.getInstance("GBP"), BigDecimal.valueOf(0.75)
        );

        when(apiService1.getExchangeRates(usd)).thenReturn(expectedRates);

        final Map<Currency, BigDecimal> result = ratesService.getRates(usd);

        assertEquals(expectedRates, result);
        verify(apiService1, times(1)).getExchangeRates(usd);
        verify(apiService2, never()).getExchangeRates(usd);
    }

    @Test
    void fallbackToSecondService() {
        final Currency usd = Currency.getInstance("USD");
        final Map<Currency, BigDecimal> expectedRates = Map.of(
                Currency.getInstance("EUR"), BigDecimal.valueOf(0.85)
        );

        when(apiService1.getExchangeRates(usd)).thenThrow(new RuntimeException("API1 failure"));
        when(apiService2.getExchangeRates(usd)).thenReturn(expectedRates);

        final Map<Currency, BigDecimal> result = ratesService.getRates(usd);

        assertEquals(expectedRates, result);
        verify(apiService1, times(1)).getExchangeRates(usd);
        verify(apiService2, times(1)).getExchangeRates(usd);
    }

    @Test
    void allApisFail() {
        final Currency usd = Currency.getInstance("USD");

        when(apiService1.getExchangeRates(usd)).thenThrow(new RuntimeException("API1 failure"));
        when(apiService2.getExchangeRates(usd)).thenThrow(new RuntimeException("API2 failure"));

        final RuntimeException exception = assertThrows(RuntimeException.class, () -> ratesService.getRates(usd));
        assertEquals("Unable to get rates for currency USD", exception.getMessage());

        verify(apiService1, times(1)).getExchangeRates(usd);
        verify(apiService2, times(1)).getExchangeRates(usd);
    }
}
