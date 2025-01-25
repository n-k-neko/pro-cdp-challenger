package com.example.procdpchallenger.application.port.outbound;

import reactor.core.publisher.Mono;

public interface ApiClientPort {
    <T> Mono<T> fetchData(String endpoint, Class<T> responseType);
}
