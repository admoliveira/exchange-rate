package com.admoliveira.exchangerate.mapper;

import com.admoliveira.exchangerate.dto.CurrencyRate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface CurrencyRateMapper {

    @Mapping(source = "key", target = "currency")
    @Mapping(source = "value", target = "rate")
    CurrencyRate toCurrencyRate(Map.Entry<String, BigDecimal> entry);

    default List<CurrencyRate> toCurrencyRateList(Map<String, BigDecimal> rates) {
        return rates.entrySet().stream()
                .map(this::toCurrencyRate)
                .toList();
    }
}
