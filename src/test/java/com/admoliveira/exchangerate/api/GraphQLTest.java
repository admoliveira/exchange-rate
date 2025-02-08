package com.admoliveira.exchangerate.api;

import com.admoliveira.exchangerate.configuration.SecurityConfig;
import com.admoliveira.exchangerate.utils.KeycloakTokenService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GraphQLTest {

    private static String validAccessToken;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void beforeAll() {
        validAccessToken = KeycloakTokenService.getAccessToken(SecurityConfig.SCOPE_GRAPHQL);
    }

    @Test
    public void noJwt() throws Exception {
        final String query = """
            {
                "query": "query {getConversions(from: \\"USD\\", amount: 100.5) { from conversions {currency conversion}}}"
            }
            """;

        mockMvc.perform(post("/graphql")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(query))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void invalidScope() throws Exception {
        final String accessToken = KeycloakTokenService.getAccessToken(SecurityConfig.SCOPE_GET_CONVERSIONS);

        final String query = """
            {
                "query": "query {getConversions(from: \\"USD\\", amount: 100.5) { from conversions {currency conversion}}}"
            }
            """;

        mockMvc.perform(post("/graphql")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(query)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getAllRates() throws Exception {
        final String query = """
        {
            "query": "query {getRates(from: \\"USD\\") { from rates {currency rate}}}"
        }
        """;

        mockMvc.perform(post("/graphql")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(query)
                        .header("Authorization", "Bearer " + validAccessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.getRates.from").value("USD"))
                .andExpect(jsonPath("$.data.getRates.rates").isArray())
                .andExpect(jsonPath("$.data.getRates.rates", hasSize(169)));
    }

    @Test
    public void getRatesWithFilter() throws Exception {
        final String query = """
        {
            "query": "query {getRates(from: \\"USD\\", to: \\"EUR\\") { from rates {currency rate}}}"
        }
        """;

        mockMvc.perform(post("/graphql")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(query)
                        .header("Authorization", "Bearer " + validAccessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.getRates.from").value("USD"))
                .andExpect(jsonPath("$.data.getRates.rates").isArray())
                .andExpect(jsonPath("$.data.getRates.rates", hasSize(1)))
                .andExpect(jsonPath("$.data.getRates.rates[0].currency").value("EUR"))
                .andExpect(jsonPath("$.data.getRates.rates[0].rate").value(0.96785));
    }

    @Test
    public void getConversions() throws Exception {
        final String query = """
            {
                "query": "query {getConversions(from: \\"USD\\", amount: 100.5) { from conversions {currency conversion}}}"
            }
            """;

        mockMvc.perform(post("/graphql")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(query)
                        .header("Authorization", "Bearer " + validAccessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.getConversions.from").value("USD"))
                .andExpect(jsonPath("$.data.getConversions.conversions").isArray())
                .andExpect(jsonPath("$.data.getConversions.conversions", hasSize(169)))
                .andExpect(jsonPath("$.data.getConversions.conversions[0].currency").exists())
                .andExpect(jsonPath("$.data.getConversions.conversions[0].conversion").exists());
    }

    @Test
    public void getConversionsWithFilter() throws Exception {
        final String query = """
            {
                "query": "query {getConversions(from: \\"USD\\", amount: 100, to: \\"EUR\\") { from conversions {currency conversion}}}"
            }
            """;

        mockMvc.perform(post("/graphql")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(query)
                        .header("Authorization", "Bearer " + validAccessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.getConversions.from").value("USD"))
                .andExpect(jsonPath("$.data.getConversions.conversions").isArray())
                .andExpect(jsonPath("$.data.getConversions.conversions", hasSize(1)))
                .andExpect(jsonPath("$.data.getConversions.conversions[0].currency").value("EUR"))
                .andExpect(jsonPath("$.data.getConversions.conversions[0].conversion").value(96.785));
    }

}
