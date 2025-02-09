package com.admoliveira.exchangerate.api;

import com.admoliveira.exchangerate.configuration.SecurityConfig;
import com.admoliveira.exchangerate.utils.KeycloakTokenService;
import com.admoliveira.exchangerate.utils.TestContainers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RatesRestApiTest extends TestContainers {

    private static String validAccessToken;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void beforeAll() {
        validAccessToken = KeycloakTokenService.getAccessToken(SecurityConfig.SCOPE_GET_RATES);
    }

    @Test
    public void getAllRatesUSD() throws Exception {
        mockMvc.perform(get("/rates")
                        .param("from", "USD")
                        .header("Authorization", "Bearer " + validAccessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.from").value("USD"))
                .andExpect(jsonPath("$.rates").isArray())
                .andExpect(jsonPath("$.rates", hasSize(169)));
    }

    @Test
    public void getAllRatesEUR() throws Exception {
        mockMvc.perform(get("/rates")
                        .param("from", "EUR")
                        .header("Authorization", "Bearer " + validAccessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.from").value("EUR"))
                .andExpect(jsonPath("$.rates").isArray())
                .andExpect(jsonPath("$.rates", hasSize(170)));
    }

    @Test
    public void getRatesWithFilter() throws Exception {
        mockMvc.perform(get("/rates")
                        .param("from", "USD")
                        .param("to", "EUR")
                        .header("Authorization", "Bearer " + validAccessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.from").value("USD"))
                .andExpect(jsonPath("$.rates").isArray())
                .andExpect(jsonPath("$.rates", hasSize(1)))
                .andExpect(jsonPath("$.rates[0].currency").value("EUR"))
                .andExpect(jsonPath("$.rates[0].rate").value(0.96785));
    }

    @Test
    public void noRatesAvailable() throws Exception {
        mockMvc.perform(get("/rates")
                        .param("from", "ZZZ")
                        .header("Authorization", "Bearer " + validAccessToken))
                .andExpect(status().isServiceUnavailable());
    }

    @Test
    public void getRatesNoJwt() throws Exception {

        mockMvc.perform(get("/rates")
                        .param("from", "USD")
                        .param("to", "EUR"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getRatesInvalidScope() throws Exception {
        final String accessToken = KeycloakTokenService.getAccessToken(SecurityConfig.SCOPE_GET_CONVERSIONS);

        mockMvc.perform(get("/rates")
                        .param("from", "USD")
                        .param("to", "EUR")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isForbidden());
    }
}
