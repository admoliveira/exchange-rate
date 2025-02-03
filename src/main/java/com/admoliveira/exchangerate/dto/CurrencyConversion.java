package com.admoliveira.exchangerate.dto;

import java.math.BigDecimal;
import java.util.Currency;

public record CurrencyConversion(Currency currency, BigDecimal conversion) {
}
