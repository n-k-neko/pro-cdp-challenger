package com.example.procdpchallenger.adapter.outbound.webclient;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.procdpchallenger.adapter.exception.WebClientException;
import com.example.procdpchallenger.adapter.outbound.webclient.mapper.ResponseMapper;
import com.example.procdpchallenger.application.port.outbound.ApiClientPort;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import reactor.core.publisher.Mono;

@Component
public class WebClientAdapter implements ApiClientPort {
    private final WebClient webClient;
    private final List<ResponseMapper<?, ?>> mappers;
    private final Map<Class<?>, String> endpointMap;
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    /**
     * WebClientAdapterのコンストラクタ。
     * @Qualifier("externalApiEndpointMap")で、ExternalApiEndpointConfig.javaで定義したBeanを取得するため、
     * 他クラスでは使用している@AllArgsConstructorを使えず、コンストラクタを使用する。
     * 
     * @param webClient WebClient   
     * @param mappers レスポンスマッパーのリスト
     * @param endpointMap エンドポイントマッピング
     * @param circuitBreakerRegistry サーキットブレーカーレジストリ
     */
    public WebClientAdapter(WebClient webClient, List<ResponseMapper<?, ?>> mappers,
            @Qualifier("externalApiEndpointMap") Map<Class<?>, String> endpointMap, CircuitBreakerRegistry circuitBreakerRegistry) {
        this.webClient = webClient;
        this.mappers = mappers;
        this.endpointMap = endpointMap;
        this.circuitBreakerRegistry = circuitBreakerRegistry;
    }

    @Override
    /*
     * 同期呼び出し
     * WebClientでblock()を行い<T>を返すことで、
     * アプリケーション層にアダプター層の詳細を隠蔽し層間の責任分担を明確にする。
     */
    public <T, R> R fetchDataSync(Class<R> domainType) {
        ResponseMapper<T, R> mapper = findMapper(domainType);
        CircuitBreaker circuitBreaker = findCircuitBreaker(domainType);
        final String endpoint = endpointMap.get(domainType);

        try {
            return circuitBreaker.executeSupplier(() -> 
                webClient.get()
                    .uri(endpoint)
                    .retrieve()
                    /*
                     * 400番台のエラーはアプリケーション側起因のエラーのため、
                     * サーキットブレーカーによって再送するのではなく、例外を投げて処理を打ち切る
                     */
                    .onStatus(status -> status.is4xxClientError(), clientResponse ->
                        // レスポンスボディをStringとして非同期に取得し、Mono<String>を返す
                        clientResponse.bodyToMono(String.class)
                            /*
                             * 上記Monoの中で非同期処理を行い、その結果を新しいMonoに変換する。
                             * 取得したボディを使って例外をスローするMono.errorを返す。
                             */
                            .flatMap(body -> Mono.error(new RuntimeException(
                                "Client error: " + clientResponse.statusCode().value() + " " + body
                            )))
                    )
                    .bodyToMono((Class<T>)mapper.getResponseType())
                    .map(mapper::mapResponse)
                    .block()
            );
        } catch (RuntimeException e) {
            // サーキットブレーカーが開いている場合や他のエラーの場合のフォールバック処理
            return handleError(domainType, e);
        }
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

    private <R> CircuitBreaker findCircuitBreaker(Class<R> domainType) {
        // domainTypeに基づいてサーキットブレーカを選択
        // TODO: application.ymlでドメインエンティティごとにサーキットブレーカーを設定する
        String circuitBreakerName = domainType.getSimpleName() + "CircuitBreaker";
        return circuitBreakerRegistry.circuitBreaker(circuitBreakerName);
    }

    private <R> R handleError(Class<R> domainType, RuntimeException e) {
        // WebClientExceptionを投げる
        // TODO: フォールバック処理を実装する
        throw new WebClientException("TEST", "Error occurred while fetching data for " + domainType.getSimpleName());
    }
}
