package com.admoliveira.exchangerate.controller;

import com.admoliveira.exchangerate.api.RatesGraphqlApi;
import com.admoliveira.exchangerate.dto.RatesResponse;
import com.admoliveira.exchangerate.mapper.RatesResponseMapper;
import com.admoliveira.exchangerate.service.RatesService;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
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
    public RatesResponse getRates(final String from, final Set<String> to) {
        final Map<String, BigDecimal> rates = ratesService.getRates(from);
        if (!CollectionUtils.isEmpty(to)) {
            rates.keySet().retainAll(to);
        }
        return ratesResponseMapper.toRatesResponse(from, rates);
    }

}
