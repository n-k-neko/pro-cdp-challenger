package com.example.procdpchallenger.adapter.outbound.webclient;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.procdpchallenger.application.port.outbound.ApiClientPort;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class WebClientAdapter implements ApiClientPort {
    private final WebClient webClient;

    @Override
    public <T> Mono<T> fetchData(String endpoint, Class<T> responseType) {
        return webClient.get()
                .uri(endpoint)
                .retrieve()
                .bodyToMono(responseType);
    }
}
