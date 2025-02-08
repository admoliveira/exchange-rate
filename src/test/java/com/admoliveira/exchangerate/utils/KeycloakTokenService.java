package com.admoliveira.exchangerate.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class KeycloakTokenService {

    private static final RestTemplate restTemplate = new RestTemplate();

    private static final String tokenUrl = "http://localhost:9090/realms/exchangerate/protocol/openid-connect/token";
    private static final String clientId = "test-client-all-scopes";
    private static final String clientSecret = "test-client-all-scopes";

    public static String getAccessToken(String scopes) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        final String requestBody = "grant_type=client_credentials" +
                             "&client_id=" + clientId +
                             "&client_secret=" + clientSecret +
                             "&scope=" + scopes;

        final HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        final ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody().get("access_token").toString();
        } else {
            throw new RuntimeException("Failed to get access token from Keycloak");
        }
    }
}
