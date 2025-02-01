package com.admoliveira.exchangerate.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

public record ConversionResponse(
        @Schema(description = "Currency", type = "string", example = "USD")
        Currency from,

        @Schema(description = "Amount", example = "100")
        BigDecimal amount,

        @Schema(
                description = "Requested conversions",
                type = "map",
                implementation = Map.class,
                example = """
                        {
                          "EUR" : 123.422,
                          "GBP" : 111.232
                        }
                        """
        )
        Map<Currency, BigDecimal> conversions) {
}
