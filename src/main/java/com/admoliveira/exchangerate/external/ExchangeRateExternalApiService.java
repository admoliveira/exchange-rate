package com.admoliveira.exchangerate.external;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

public interface ExchangeRateExternalApiService {
    Map<Currency, BigDecimal> getExchangeRates(final Currency currency);
    int getPriority();
}
