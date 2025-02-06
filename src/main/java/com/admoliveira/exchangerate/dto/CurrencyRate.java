package com.admoliveira.exchangerate.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.Currency;

public record CurrencyRate(
        @Schema(description = "The target currency for the exchange rate.", type = "string", example = "EUR")
        Currency currency,

        @Schema(description = "The exchange rate relative to the base currency.", example = "1.032")
        BigDecimal rate) {
}
