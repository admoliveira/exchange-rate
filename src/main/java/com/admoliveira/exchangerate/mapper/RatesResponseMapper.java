package com.admoliveira.exchangerate.mapper;

import com.admoliveira.exchangerate.dto.RatesResponse;
import org.mapstruct.Mapper;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

@Mapper(componentModel = "spring", uses = CurrencyRateMapper.class)
public interface RatesResponseMapper {

    RatesResponse toRatesResponse(Currency from, Map<Currency, BigDecimal> rates);

}