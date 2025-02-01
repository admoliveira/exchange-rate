package com.admoliveira.exchangerate.external.exchangeratehost.dto;

import java.math.BigDecimal;
import java.util.Map;

public record ExchangerateHostLiveResponse(
        boolean success,
        String terms,
        String privacy,
        long timestamp,
        String source,
        Map<String, BigDecimal> quotes) {
}
