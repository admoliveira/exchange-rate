package com.admoliveira.exchangerate.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Currency;
import java.util.List;

public record RatesResponse(
        @Schema(description = "The base currency for the exchange rates.", type = "string", example = "USD")
        Currency from,

        @Schema(description = "A list of exchange rates for the specified target currencies.")
        List<CurrencyRate> rates) {
}
