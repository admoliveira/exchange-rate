package com.admoliveira.exchangerate.mapper;

import com.admoliveira.exchangerate.dto.CurrencyConversion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface CurrencyConversionMapper {

    @Mapping(source = "key", target = "currency")
    @Mapping(source = "value", target = "conversion")
    CurrencyConversion toCurrencyConversion(Map.Entry<Currency, BigDecimal> entry);

    default List<CurrencyConversion> toCurrencyConversionList(Map<Currency, BigDecimal> conversions) {
        return conversions.entrySet().stream()
                .map(this::toCurrencyConversion)
                .toList();
    }
}
