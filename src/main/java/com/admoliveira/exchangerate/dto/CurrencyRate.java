package com.admoliveira.exchangerate.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record CurrencyRate(
        @Schema(description = "The target currency for the exchange rate.", type = "string", example = "EUR")
        String currency,

        @Schema(description = "The exchange rate relative to the base currency.", example = "1.032")
        BigDecimal rate) {
}
