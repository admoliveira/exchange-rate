package com.admoliveira.exchangerate.dto;

import java.math.BigDecimal;
import java.util.Currency;

public record CurrencyRate(Currency currency, BigDecimal rate) {
}