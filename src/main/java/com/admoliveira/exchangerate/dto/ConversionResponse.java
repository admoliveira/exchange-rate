package com.admoliveira.exchangerate.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

public record ConversionResponse(
        @Schema(description = "The base currency of the conversion.", type = "string", example = "USD")
        Currency from,

        @Schema(description = "The amount to be converted.", example = "100")
        BigDecimal amount,

        @Schema(description = "A list of conversion results for the specified target currencies.")
        List<CurrencyConversion> conversions) {
}