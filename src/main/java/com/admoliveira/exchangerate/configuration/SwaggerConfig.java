package com.admoliveira.exchangerate.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI springOpenApiConfig() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("api_key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .description("Api Key")
                                        .in(SecurityScheme.In.HEADER)
                                        .name("X-API-Key")))
                .security(List.of(new SecurityRequirement().addList("api_key")));
    }
}
