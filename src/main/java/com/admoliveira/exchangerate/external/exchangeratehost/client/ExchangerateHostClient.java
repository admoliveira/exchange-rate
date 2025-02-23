package com.admoliveira.exchangerate.external.exchangeratehost.client;

import com.admoliveira.exchangerate.external.exchangeratehost.dto.ExchangerateHostLiveResponse;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface ExchangerateHostClient {

    @GetExchange(value = "/live", accept = "application/json")
    ExchangerateHostLiveResponse getLive(@RequestParam(value = "source") String source);
}
