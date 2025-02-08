package com.admoliveira.exchangerate.external;

import java.math.BigDecimal;
import java.util.Map;

public interface ExchangeRateExternalApiService {
    Map<String, BigDecimal> getExchangeRates(final String currency);

    int getPriority();
}
