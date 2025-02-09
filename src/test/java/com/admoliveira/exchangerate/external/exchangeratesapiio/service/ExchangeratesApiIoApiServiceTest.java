package com.admoliveira.exchangerate.external.exchangeratesapiio.service;

import com.admoliveira.exchangerate.external.exchangeratesapiio.client.ExchangeratesApiIoClient;
import com.admoliveira.exchangerate.external.exchangeratesapiio.dto.ExchangeratesApiIoLatestResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeratesApiIoApiServiceTest {

    @Mock
    private ExchangeratesApiIoClient mockClient;

    @Mock
    private ExchangeratesApiIoApiServiceConfigProperties mockConfigProperties;

    @InjectMocks
    private ExchangeratesApiIoApiService service;

    @Test
    void getExchangeRates() {
        final String baseCurrency = "USD";

        final Map<String, BigDecimal> mockQuotes = Map.of(
                "EUR", new BigDecimal("0.85"),
                "GBP", new BigDecimal("0.75")
        );

        final ExchangeratesApiIoLatestResponse response = new ExchangeratesApiIoLatestResponse(
                true,
                System.currentTimeMillis(),
                baseCurrency,
                LocalDate.now(),
                mockQuotes
        );

        when(mockClient.getLatest(eq(baseCurrency))).thenReturn(response);

        final Map<String, BigDecimal> exchangeRates = service.getExchangeRates(baseCurrency);

        assertNotNull(exchangeRates);
        assertEquals(2, exchangeRates.size());
        assertEquals(new BigDecimal("0.85"), exchangeRates.get("EUR"));
        assertEquals(new BigDecimal("0.75"), exchangeRates.get("GBP"));
    }

    @Test
    void notSuccess() {
        final String baseCurrency = "USD";
        final ExchangeratesApiIoLatestResponse response = new ExchangeratesApiIoLatestResponse(
                false,
                System.currentTimeMillis(),
                baseCurrency,
                LocalDate.now(),
                null
        );

        when(mockClient.getLatest(eq(baseCurrency))).thenReturn(response);

        final RuntimeException exception = assertThrows(RuntimeException.class, () -> service.getExchangeRates(baseCurrency));
        assertEquals("Error getting exchange rates from ExchangeratesApiIo for currency: " + baseCurrency, exception.getMessage());
    }

    @Test
    void throwException() {
        final String baseCurrency = "USD";

        when(mockClient.getLatest(eq(baseCurrency))).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class, () -> service.getExchangeRates(baseCurrency));
    }

    @Test
    void getPriority() {
        final int expectedPriority = 50;
        when(mockConfigProperties.priority()).thenReturn(expectedPriority);

        final int priority = service.getPriority();

        assertEquals(expectedPriority, priority);
    }
}
