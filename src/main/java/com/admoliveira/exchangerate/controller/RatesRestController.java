package com.admoliveira.exchangerate.controller;

import com.admoliveira.exchangerate.api.RatesRestApi;
import com.admoliveira.exchangerate.dto.RatesResponse;
import com.admoliveira.exchangerate.mapper.RatesResponseMapper;
import com.admoliveira.exchangerate.service.RatesService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;
import java.util.Set;


@RestController
public class RatesRestController implements RatesRestApi {

    public final RatesService ratesService;
    public final RatesResponseMapper ratesResponseMapper;

    public RatesRestController(final RatesService ratesService,
                               final RatesResponseMapper ratesResponseMapper) {
        this.ratesService = ratesService;
        this.ratesResponseMapper = ratesResponseMapper;
    }

    @Override
    public RatesResponse getRates(final Currency from, final Set<Currency> to) {
        Map<Currency, BigDecimal> rates = ratesService.getRates(from);
        if (!CollectionUtils.isEmpty(to)) {
            rates.keySet().retainAll(to);
        }
        return ratesResponseMapper.toRatesResponse(from, rates);
    }

}
