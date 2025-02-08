package com.admoliveira.exchangerate.api;

import com.admoliveira.exchangerate.configuration.SecurityConfig;
import com.admoliveira.exchangerate.utils.KeycloakTokenService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
class ConversionRestApiTest {

    private static String validAccessToken;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void beforeAll() {
        validAccessToken = KeycloakTokenService.getAccessToken(SecurityConfig.SCOPE_GET_CONVERSIONS);
    }

    @Test
    public void getConversionsUSD() throws Exception {
        mockMvc.perform(get("/conversions")
                        .param("from", "USD")
                        .param("amount", "100")
                        .header("Authorization", "Bearer " + validAccessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.from").value("USD"))
                .andExpect(jsonPath("$.amount").value("100"))
                .andExpect(jsonPath("$.conversions").isArray())
                .andExpect(jsonPath("$.conversions", hasSize(169)));
    }

    @Test
    public void getConversionsEUR() throws Exception {
        mockMvc.perform(get("/conversions")
                        .param("from", "EUR")
                        .param("amount", "100")
                        .header("Authorization", "Bearer " + validAccessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.from").value("EUR"))
                .andExpect(jsonPath("$.amount").value("100"))
                .andExpect(jsonPath("$.conversions").isArray())
                .andExpect(jsonPath("$.conversions", hasSize(170)));
    }

    @Test
    public void getConversionsWithFilter() throws Exception {
        mockMvc.perform(get("/conversions")
                        .param("from", "USD")
                        .param("amount", "100")
                        .param("to", "EUR")
                        .header("Authorization", "Bearer " + validAccessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.from").value("USD"))
                .andExpect(jsonPath("$.amount").value("100"))
                .andExpect(jsonPath("$.conversions").isArray())
                .andExpect(jsonPath("$.conversions[0].currency").value("EUR"))
                .andExpect(jsonPath("$.conversions[0].conversion").value(96.785));
    }

    @Test
    public void noRatesAvailable() throws Exception {
        mockMvc.perform(get("/conversions")
                        .param("from", "ZZZ")
                        .param("amount", "100")
                        .header("Authorization", "Bearer " + validAccessToken))
                .andExpect(status().isServiceUnavailable());
    }

    @ParameterizedTest
    @ValueSource(strings = {"INVALID", ",", "-1.1", "0"})
    public void invalidAmount(final String amount) throws Exception {
        mockMvc.perform(get("/conversions")
                        .param("from", "USD")
                        .param("amount", amount)
                        .header("Authorization", "Bearer " + validAccessToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getConversionsNoJwt() throws Exception {

        mockMvc.perform(get("/conversions")
                        .param("from", "USD")
                        .param("amount", "100"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getConversionsInvalidScope() throws Exception {
        final String accessToken = KeycloakTokenService.getAccessToken(SecurityConfig.SCOPE_GET_RATES);

        mockMvc.perform(get("/conversions")
                        .param("from", "USD")
                        .param("amount", "100")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isForbidden());
    }
}