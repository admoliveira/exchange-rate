package com.admoliveira.exchangerate.api;

import com.admoliveira.exchangerate.dto.ConversionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.DecimalMin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Set;


@RequestMapping("/conversions")
@Tag(name = "Conversions", description = "Provides currency conversion capabilities.")
public interface ConversionRestApi {

    @GetMapping(produces = "application/json")
    @Operation(
            summary = "Retrieve currency conversions",
            description = "Converts a specified amount from a source currency to one or more target currencies."
    )
    ConversionResponse getConversions(
            @Schema(description = "The source currency for conversion.", example = "USD")
            @RequestParam(value = "from") Currency from,

            @Schema(
                    description = "A comma-separated list of target currencies. " +
                            "If omitted, conversions for all supported currencies will be returned.",
                    type = "string",
                    example = "EUR,GBP"
            )
            @RequestParam(value = "to", required = false) Set<Currency> to,

            @Schema(description = "The amount to be converted.", example = "50.5")
            @DecimalMin(value = "0.0", inclusive = false)
            @RequestParam(value = "amount") BigDecimal amount);
}
