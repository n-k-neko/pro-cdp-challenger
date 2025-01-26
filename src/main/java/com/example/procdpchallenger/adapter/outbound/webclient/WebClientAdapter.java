package com.example.procdpchallenger.adapter.outbound.webclient;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.procdpchallenger.adapter.outbound.webclient.mapper.ResponseMapper;
import com.example.procdpchallenger.application.port.outbound.ApiClientPort;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class WebClientAdapter implements ApiClientPort {
    private final WebClient webClient;
    private final List<ResponseMapper<?, ?>> mappers;

    @Override
    public <T, R> R fetchData(String endpoint, Class<R> domainType) {
        ResponseMapper<T, R> mapper = findMapper(domainType);
        
        // TODO：呼び出し元でblock()するかどうかを検討する.呼び出し元で制御したほうが拡張性が高いのでは。
        return webClient.get()
                .uri(endpoint)
                .retrieve()
                .bodyToMono((Class<T>)mapper.getResponseType())
                .map(mapper::mapResponse)
                .block();
    }

    @SuppressWarnings("unchecked")
    private <T, R> ResponseMapper<T, R> findMapper(Class<R> domainType) {
        return (ResponseMapper<T, R>) mappers.stream()
                .filter(mapper -> isValidMapper(mapper, domainType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No mapper found for domainType: " + domainType.getName()));
    }

    private boolean isValidMapper(ResponseMapper<?, ?> mapper, Class<?> domainType) {
        return mapper.getDomainType().equals(domainType);
    }
}
