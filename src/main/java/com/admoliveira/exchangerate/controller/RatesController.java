package com.admoliveira.exchangerate.controller;

import com.admoliveira.exchangerate.api.RatesApi;
import com.admoliveira.exchangerate.dto.RatesResponse;
import com.admoliveira.exchangerate.service.RatesService;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;
import java.util.Set;

@RestController
public class RatesController implements RatesApi {

    public final RatesService ratesService;

    public RatesController(RatesService ratesService) {
        this.ratesService = ratesService;
    }

    @Override
    public RatesResponse getRates(final Currency from, final Set<Currency> to) {
        final Map<Currency, BigDecimal> rates = ratesService.getRates(from, to);
        return new RatesResponse(from, rates);
    }
}
