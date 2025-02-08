package com.admoliveira.exchangerate.external.exchangeratehost.service;

import com.admoliveira.exchangerate.external.exchangeratehost.client.ExchangerateHostClient;
import com.admoliveira.exchangerate.external.exchangeratehost.dto.ExchangerateHostLiveResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangerateHostApiServiceTest {

    @Mock
    private ExchangerateHostClient mockClient;

    @Mock
    private ExchangerateHostApiServiceConfigProperties mockConfigProperties;

    @InjectMocks
    private ExchangerateHostApiService service;

    @Test
    void getExchangeRates() {
        final String baseCurrency ="USD";

        final Map<String, BigDecimal> mockQuotes = Map.of(
                "USDEUR", new BigDecimal("0.85"),
                "USDGBP", new BigDecimal("0.75")
        );

        final ExchangerateHostLiveResponse response = new ExchangerateHostLiveResponse(
                true,
                "https://www.example.com/terms",
                "https://www.example.com/privacy",
                System.currentTimeMillis(),
                "USD",
                mockQuotes
        );
        when(mockClient.getLive(eq(baseCurrency))).thenReturn(response);

        final Map<String, BigDecimal> exchangeRates = service.getExchangeRates(baseCurrency);

        assertNotNull(exchangeRates);
        assertEquals(2, exchangeRates.size());
        assertEquals(new BigDecimal("0.85"), exchangeRates.get("EUR"));
        assertEquals(new BigDecimal("0.75"), exchangeRates.get("GBP"));
    }

    @Test
    void getPriority() {
        final int expectedPriority = 50;
        when(mockConfigProperties.priority()).thenReturn(expectedPriority);

        final int priority = service.getPriority();

        assertEquals(expectedPriority, priority);
    }
}
