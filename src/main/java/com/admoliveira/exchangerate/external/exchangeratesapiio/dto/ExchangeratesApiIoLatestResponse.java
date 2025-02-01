package com.admoliveira.exchangerate.external.exchangeratesapiio.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Map;

public record ExchangeratesApiIoLatestResponse(
        boolean success,
        long timestamp,
        Currency base,
        LocalDate date,
        Map<Currency, BigDecimal> rates) {
}
