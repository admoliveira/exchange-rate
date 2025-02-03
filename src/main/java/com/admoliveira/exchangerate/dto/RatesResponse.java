package com.admoliveira.exchangerate.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Currency;
import java.util.List;
import java.util.Map;

public record RatesResponse(
        @Schema(description = "Currency", type = "string", example = "USD")
        Currency from,

        @Schema(
                description = "Rates for the requested currency",
                type = "map",
                implementation = Map.class,
                example = """
                        {
                          "EUR" : 1.23422,
                          "GBP" : 1.11232
                        }
                        """
        )
        List<CurrencyRate> rates) {
}
