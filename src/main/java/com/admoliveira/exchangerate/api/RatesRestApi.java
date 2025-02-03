package com.admoliveira.exchangerate.api;

import com.admoliveira.exchangerate.dto.RatesResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Currency;
import java.util.Set;

@RequestMapping("/rates")
public interface RatesRestApi {

    @GetMapping(produces = "application/json")
    RatesResponse getRates(
            @Schema(description = "Currency to get rates from", example = "USD")
            @RequestParam(value = "from") Currency from,

            @Schema(description = "Comma separated list of currencies to get rates to. " +
                    "Will return rates for all available currencies if empty or not provided.",
                    type = "string",
                    example = "EUR,GBP"
            )
            @RequestParam(value = "to", required = false) Set<Currency> to);
}
