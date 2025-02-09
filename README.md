# Exchange Rate

## Overview
`exchange-rate` is a service that provides exchange rate information via a REST API. It integrates with two external APIs to fetch data:
- https://exchangerate.host
- https://exchangeratesapi.io

The APIs are chosen based on a priority setting. By default, https://exchangerate.host is used first, and if it fails to retrieve rates, the service falls back to https://exchangeratesapi.io.

## Prerequisites
Ensure you have the following installed:
- Docker

## How to Run
To build and start the service, run the following commands:

```sh
docker compose up --build
```

This will build the application and start it within a Docker container.

By default, the service uses Wiremock for mocking external APIs, and when using Wiremock, the service only has rates from USD and EUR. To use real APIs, comment out the environment variables containing the base URLs in the `docker-compose.yaml` and replace the API keys with valid ones:
```yaml
# To use real APIs, comment ...BASE_URL vars and set the API keys
# EXCHANGE_RATE_EXTERNAL_EXCHANGERATE_HOST_CLIENT_BASE_URL: http://wiremock:8080/exchangeratehost
EXCHANGE_RATE_EXTERNAL_EXCHANGERATE_HOST_CLIENT_API_KEY: your-real-api-key
# EXCHANGE_RATE_EXTERNAL_EXCHANGERATES_API_IO_CLIENT_BASE_URL: http://wiremock:8080/exchangeratesapiio
EXCHANGE_RATE_EXTERNAL_EXCHANGERATES_API_IO_CLIENT_API_KEY: your-real-api-key
```

## Development Mode
### Prerequisites
Ensure you have the following installed:
- Docker
- Java 21

To run the service in development mode, use the following steps:

1. Start the required dependencies:
   ```sh
   docker compose up -d keycloak redis wiremock
   ```

2. Run the service with dev profile:
   ```sh
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
   ```

To compile the service and run tests:
   ```sh
   ./mvnw clean install
   ```

## How to Use the API
Swagger is available at http://localhost:8080/swagger-ui/index.html and can be used to test the API.

The API is authenticated with JWT. By default, Keycloak is configured with a client that has the required scopes to call all APIs:

- **Client ID:** `test-client-all-scopes`
- **Client Secret:** `test-client-all-scopes`

Authentication can be performed directly in Swagger by clicking the **Authorize** button.

To manually generate a token, replace `<scope>` with the required scopes and run the following command:

```sh
curl -X POST "http://localhost:9090/realms/exchangerate/protocol/openid-connect/token" \
     -H "Content-Type: application/x-www-form-urlencoded" \
     -d "grant_type=client_credentials" \
     -d "scope=<scope>" \
     -d "client_id=test-client-all-scopes" \
     -d "client_secret=test-client-all-scopes"
```

### Available Scopes:
- `get-rates`: Required to call rates API
- `get-conversions`: Required to call conversions API
- `graphql`: Required to call graphql API
