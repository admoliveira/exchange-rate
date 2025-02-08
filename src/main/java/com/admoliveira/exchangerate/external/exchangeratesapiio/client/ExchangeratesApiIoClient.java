package com.admoliveira.exchangerate.external.exchangeratesapiio.client;

import com.admoliveira.exchangerate.external.exchangeratesapiio.dto.ExchangeratesApiIoLatestResponse;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/v1")
public interface ExchangeratesApiIoClient {

    @GetExchange(value = "/latest", accept = "application/json")
    ExchangeratesApiIoLatestResponse getLatest(@RequestParam(value = "base") String base);
}
