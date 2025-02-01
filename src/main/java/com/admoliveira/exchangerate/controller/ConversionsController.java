package com.admoliveira.exchangerate.controller;

import com.admoliveira.exchangerate.api.ConversionApi;
import com.admoliveira.exchangerate.dto.ConversionResponse;
import com.admoliveira.exchangerate.service.ConversionService;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;
import java.util.Set;

@RestController
public class ConversionsController implements ConversionApi {

    private final ConversionService conversionService;

    public ConversionsController(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public ConversionResponse getConversions(final Currency from, final Set<Currency> to, final BigDecimal amount) {
        final Map<Currency, BigDecimal> conversions = conversionService.getConversions(from, to, amount);
        return new ConversionResponse(from, amount, conversions);
    }
}
