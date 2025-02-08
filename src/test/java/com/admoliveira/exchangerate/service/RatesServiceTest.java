package com.admoliveira.exchangerate.service;

import com.admoliveira.exchangerate.exception.UnavailableRatesException;
import com.admoliveira.exchangerate.external.ExchangeRateExternalApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
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
        final String usd = "USD";
        final Map<String, BigDecimal> expectedRates = Map.of(
            "EUR", BigDecimal.valueOf(0.85),
              "GBP", BigDecimal.valueOf(0.75)
        );

        when(apiService1.getExchangeRates(usd)).thenReturn(expectedRates);

        final Map<String, BigDecimal> result = ratesService.getRates(usd);

        assertEquals(expectedRates, result);
        verify(apiService1, times(1)).getExchangeRates(usd);
        verify(apiService2, never()).getExchangeRates(usd);
    }

    @Test
    void fallbackToSecondService() {
        final String usd = "USD";
        final Map<String, BigDecimal> expectedRates = Map.of(
              "EUR", BigDecimal.valueOf(0.85)
        );

        when(apiService1.getExchangeRates(usd)).thenThrow(new RuntimeException("API1 failure"));
        when(apiService2.getExchangeRates(usd)).thenReturn(expectedRates);

        final Map<String, BigDecimal> result = ratesService.getRates(usd);

        assertEquals(expectedRates, result);
        verify(apiService1, times(1)).getExchangeRates(usd);
        verify(apiService2, times(1)).getExchangeRates(usd);
    }

    @Test
    void allApisFail() {
        final String usd = "USD";

        when(apiService1.getExchangeRates(usd)).thenThrow(new RuntimeException("API1 failure"));
        when(apiService2.getExchangeRates(usd)).thenThrow(new RuntimeException("API2 failure"));

        final UnavailableRatesException exception = assertThrows(UnavailableRatesException.class, () -> ratesService.getRates(usd));

        verify(apiService1, times(1)).getExchangeRates(usd);
        verify(apiService2, times(1)).getExchangeRates(usd);
    }
}
