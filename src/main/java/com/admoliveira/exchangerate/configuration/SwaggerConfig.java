package com.admoliveira.exchangerate.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static com.admoliveira.exchangerate.configuration.SecurityConfig.SCOPE_GET_CONVERSIONS;
import static com.admoliveira.exchangerate.configuration.SecurityConfig.SCOPE_GET_RATES;


@Configuration
public class SwaggerConfig {

    @Value("${exchange-rate.swagger-config.issuer-uri}")
    private String issuerUri;

    @Bean
    public OpenAPI springOpenApiConfig() {
        final Scopes scopes = new Scopes();
        scopes.put(SCOPE_GET_RATES, "Required to get rates");
        scopes.put(SCOPE_GET_CONVERSIONS, "Required to get conversions");

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("client_credentials",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.OAUTH2)
                                        .flows(new OAuthFlows()
                                                .clientCredentials(new OAuthFlow()
                                                        .scopes(scopes)
                                                        .tokenUrl(issuerUri)))))
                .security(List.of(new SecurityRequirement().addList("client_credentials")));
    }

}
