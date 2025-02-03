package com.admoliveira.exchangerate.controller;

import com.admoliveira.exchangerate.api.RatesGraphqlApi;
import com.admoliveira.exchangerate.dto.RatesResponse;
import com.admoliveira.exchangerate.mapper.RatesResponseMapper;
import com.admoliveira.exchangerate.service.RatesService;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;
import java.util.Set;

@Controller
public class RatesGraphqlController implements RatesGraphqlApi {

    public final RatesService ratesService;
    public final RatesResponseMapper ratesResponseMapper;

    public RatesGraphqlController(final RatesService ratesService,
                                  final RatesResponseMapper ratesResponseMapper) {
        this.ratesService = ratesService;
        this.ratesResponseMapper = ratesResponseMapper;
    }

    @Override
    public RatesResponse getRates(final Currency from, final Set<Currency> to) {
        final Map<Currency, BigDecimal> rates = ratesService.getRates(from, to);
        return ratesResponseMapper.toRatesResponse(from, rates);
    }
}
