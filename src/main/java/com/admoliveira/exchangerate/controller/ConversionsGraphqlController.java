package com.admoliveira.exchangerate.controller;

import com.admoliveira.exchangerate.api.ConversionGraphqlApi;
import com.admoliveira.exchangerate.dto.ConversionResponse;
import com.admoliveira.exchangerate.mapper.ConversionResponseMapper;
import com.admoliveira.exchangerate.service.ConversionService;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

@Controller
public class ConversionsGraphqlController implements ConversionGraphqlApi {

    private final ConversionService conversionService;
    private final ConversionResponseMapper conversionResponseMapper;

    public ConversionsGraphqlController(final ConversionService conversionService,
                                        final ConversionResponseMapper conversionResponseMapper) {
        this.conversionService = conversionService;
        this.conversionResponseMapper = conversionResponseMapper;
    }

    @Override
    public ConversionResponse getConversions(final String from, final Set<String> to, final BigDecimal amount) {
        final Map<String, BigDecimal> conversions = conversionService.getConversions(from, to, amount);
        return conversionResponseMapper.toConversionResponse(from, amount, conversions);
    }
}
