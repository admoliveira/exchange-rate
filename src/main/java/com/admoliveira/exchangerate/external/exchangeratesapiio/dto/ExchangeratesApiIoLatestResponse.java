package com.admoliveira.exchangerate.external.exchangeratesapiio.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public record ExchangeratesApiIoLatestResponse(
        boolean success,
        long timestamp,
        String base,
        LocalDate date,
        Map<String, BigDecimal> rates) {
}
