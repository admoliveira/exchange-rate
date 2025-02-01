package com.admoliveira.exchangerate.devmock;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Currency;

@RestController
public class DevmockController {

    @GetMapping("/devmock/exchangerate/live")
    String getRates(
            @RequestParam(value = "source") Currency source){
        return """
                {
                    "success": true,
                    "terms": "https://exchangerate.host/terms",
                    "privacy": "https://exchangerate.host/privacy",
                    "timestamp": 1432400348,
                    "source": "USD",
                    "quotes": {
                        "USDAUD": 1.278342,
                        "USDEUR": 1.278342,
                        "USDGBP": 0.908019,
                        "USDPLN": 3.731504
                    }
                }
                
                """;
    }

    @GetMapping("/devmock/exchangeratesapiio/v1/latest")
    String getRatesExchangeratesApiIo(
            @RequestParam(value = "base") Currency base){
        return """
                {
                  "success": true,
                    "timestamp": 1519296206,
                    "base": "USD",
                    "date": "2021-03-17",
                    "rates": {
                        "GBP": 0.72007,
                        "JPY": 107.346001,
                        "EUR": 0.813399
                    }
                }
                
                """;
    }
}
