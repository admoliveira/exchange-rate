package com.admoliveira.exchangerate.external.exchangeratehost.client;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.lang.NonNull;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Configuration
@EnableConfigurationProperties(ExchangerateHostClientConfigProperties.class)
public class ExchangerateHostClientConfig {

    @Bean
    public ExchangerateHostClient exchangerateHostClient(
            final ExchangerateHostClientConfigProperties configProperties) {

        final RestClient restClient = RestClient.builder()
                .baseUrl(configProperties.baseUrl())
                .requestInterceptor((request, body, execution) -> {
                    final HttpRequest modifiedRequest = new HttpRequestWrapper(request) {
                        @Override
                        @NonNull
                        public URI getURI() {
                            return UriComponentsBuilder.fromUri(request.getURI())
                                    .queryParam("access_key", configProperties.apiKey())
                                    .build()
                                    .toUri();
                        }
                    };
                    return execution.execute(modifiedRequest, body);
                })
                .build();

        final RestClientAdapter adapter = RestClientAdapter.create(restClient);
        final HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(ExchangerateHostClient.class);
    }
}
