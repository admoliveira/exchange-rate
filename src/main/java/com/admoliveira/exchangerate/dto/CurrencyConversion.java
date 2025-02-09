package com.admoliveira.exchangerate.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record CurrencyConversion(
        @Schema(description = "The target currency for the conversion.", type = "string", example = "EUR")
        String currency,

        @Schema(description = "The converted amount in the target currency.", example = "102.5")
        BigDecimal conversion) {
}
