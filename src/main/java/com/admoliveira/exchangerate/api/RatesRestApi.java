package com.admoliveira.exchangerate.api;

import com.admoliveira.exchangerate.dto.RatesResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Currency;
import java.util.Set;

@RequestMapping("/rates")
@Tag(name = "Rates", description = "Provides exchange rate data for various currencies.")
public interface RatesRestApi {

    @GetMapping(produces = "application/json")
    @Operation(
            summary = "Retrieve exchange rates",
            description = "Returns exchange rates from a specified source currency to one or more target currencies."
    )
    RatesResponse getRates(
            @Schema(description = "The base currency for retrieving exchange rates.", example = "USD")
            @RequestParam(value = "from") Currency from,

            @Schema(
                    description = "A comma-separated list of target currencies. " +
                            "If omitted, rates for all supported currencies will be returned.",
                    type = "string",
                    example = "EUR,GBP"
            )
            @RequestParam(value = "to", required = false) Set<Currency> to);
}