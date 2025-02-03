package com.admoliveira.exchangerate.mapper;

import com.admoliveira.exchangerate.dto.ConversionResponse;
import org.mapstruct.Mapper;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

@Mapper(componentModel = "spring", uses = CurrencyConversionMapper.class)
public interface ConversionResponseMapper {

    ConversionResponse toConversionResponse(Currency from,
                                            BigDecimal amount,
                                            Map<Currency, BigDecimal> conversions);
}
