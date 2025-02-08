package com.admoliveira.exchangerate.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public static final String SCOPE_GET_RATES = "get-rates";
    public static final String SCOPE_GET_CONVERSIONS = "get-conversions";
    public static final String SCOPE_GRAPHQL = "graphql";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/rates").hasAuthority("SCOPE_" + SCOPE_GET_RATES)
                                .requestMatchers("/conversions").hasAuthority("SCOPE_" + SCOPE_GET_CONVERSIONS)
                                .requestMatchers("/graphql").hasAuthority("SCOPE_" + SCOPE_GRAPHQL)
                                .anyRequest().permitAll())
                .oauth2ResourceServer((oauth2) -> oauth2
                        .jwt(Customizer.withDefaults()));
        return http.build();
    }

}
