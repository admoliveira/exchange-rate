package com.admoliveira.exchangerate.api;

import com.admoliveira.exchangerate.dto.ConversionResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Set;

@RequestMapping("/conversions")
public interface ConversionRestApi {

    @GetMapping(produces = "application/json")
    ConversionResponse getConversions(
            @Schema(description = "Currency to get conversion from", example = "USD")
            @RequestParam(value = "from") Currency from,

            @Schema(description = "Comma separated list of currencies to convert to. " +
                    "Will return conversions for all available currencies if empty or not provided.",
                    type = "string",
                    example = "EUR,GBP"
            )
            @RequestParam(value = "to", required = false) Set<Currency> to,

            @Schema(description = "Amount to be converted", example = "50.5")
            @RequestParam(value = "amount") BigDecimal amount);
}
