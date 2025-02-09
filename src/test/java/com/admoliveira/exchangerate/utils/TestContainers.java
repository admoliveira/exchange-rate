package com.admoliveira.exchangerate.utils;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.MountableFile;

public class TestContainers {

    private final static GenericContainer<?> keycloakContainer = new GenericContainer("keycloak/keycloak:26.1")
            .withCommand("start-dev --import-realm --verbose")
            .withExposedPorts(8080)
            .withEnv("KEYCLOAK_ADMIN", "admin")
            .withEnv("KEYCLOAK_ADMIN_PASSWORD", "admin")
            .withCopyFileToContainer(MountableFile.forClasspathResource("keycloak/keycloak-dev-config.json"), "/opt/keycloak/data/import/realm-config.json")
            .waitingFor(Wait.forHttp("/").forStatusCode(200));

    static final GenericContainer<?> wireMockContainer = new GenericContainer("wiremock/wiremock:3.11.0")
            .withExposedPorts(8080)
            .withCopyFileToContainer(MountableFile.forClasspathResource("wiremock/mappings"), "/home/wiremock/mappings")
            .withCopyFileToContainer(MountableFile.forClasspathResource("wiremock/__files"), "/home/wiremock/__files")
            .waitingFor(Wait.forHttp("/__admin").forStatusCode(200));

    static final GenericContainer<?> redisContainer = new GenericContainer("redis:7.4.2")
            .withCommand("redis-server --appendonly yes --bind 0.0.0.0")
            .withExposedPorts(6379)
            .waitingFor(Wait.forListeningPort());


    static {
        keycloakContainer.start();
        wireMockContainer.start();
        redisContainer.start();

        final String keycloakHost = keycloakContainer.getHost();
        final Integer keycloakPort = keycloakContainer.getMappedPort(8080);
        final String issuerUri = String.format("http://%s:%d/realms/exchangerate", keycloakHost, keycloakPort);

        KeycloakTokenService.tokenUrl = issuerUri + "/protocol/openid-connect/token";
        System.setProperty("spring.security.oauth2.resourceserver.jwt.issuer-uri", issuerUri);
        System.setProperty("exchange-rate.swagger-config.issuer-uri", issuerUri + "/protocol/openid-connect/token");

        final String wireMockHost = wireMockContainer.getHost();
        final Integer wireMockPort = wireMockContainer.getMappedPort(8080);
        final String wireMockBaseUrl = String.format("http://%s:%d", wireMockHost, wireMockPort);

        System.setProperty("exchange-rate.external.exchangerate-host-client.base-url", wireMockBaseUrl + "/exchangeratehost");
        System.setProperty("exchange-rate.external.exchangerates-api-io-client.base-url", wireMockBaseUrl + "/exchangeratesapiio");

        System.setProperty("spring.data.redis.host", redisContainer.getHost());
        System.setProperty("spring.data.redis.port", redisContainer.getMappedPort(6379).toString());
    }

}
