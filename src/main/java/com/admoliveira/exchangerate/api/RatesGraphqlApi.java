package com.admoliveira.exchangerate.api;

import com.admoliveira.exchangerate.dto.RatesResponse;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;

import java.util.Currency;
import java.util.Set;

public interface RatesGraphqlApi {

    @QueryMapping
    RatesResponse getRates(@Argument Currency from, @Argument Set<Currency> to);
}
