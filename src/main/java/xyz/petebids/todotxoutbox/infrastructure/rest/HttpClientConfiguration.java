package xyz.petebids.todotxoutbox.infrastructure.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.server.resource.web.reactive.function.client.ServerBearerExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class HttpClientConfiguration {

    @Value("${keycloak:http://localhost:4080}")
    String serviceUrl;

    @Bean
    public KeycloakClient keycloakClient() {

        WebClient webClient = WebClient.builder()
                .baseUrl(serviceUrl)
                .filter(new ServerBearerExchangeFilterFunction())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builder()
                .clientAdapter(WebClientAdapter.forClient(webClient))
                .build();

        return httpServiceProxyFactory.createClient(KeycloakClient.class);

    }
}
