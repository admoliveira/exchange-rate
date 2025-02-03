package com.admoliveira.exchangerate.api;

import com.admoliveira.exchangerate.dto.ConversionResponse;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Set;

public interface ConversionGraphqlApi {

    @QueryMapping
    ConversionResponse getConversions(@Argument Currency from,
                                      @Argument Set<Currency> to,
                                      @Argument BigDecimal amount);
}
