package com.example.procdpchallenger.adapter.outbound.webclient;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.example.procdpchallenger.adapter.exception.WebClientException;
import com.example.procdpchallenger.adapter.outbound.webclient.mapper.ResponseMapper;
import com.example.procdpchallenger.application.port.outbound.ApiClientPort;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import com.example.procdpchallenger.shared.exception.ErrorCodes;

@Component
public class WebClientAdapter implements ApiClientPort {
    private final WebClient webClient;
    private final List<ResponseMapper<?, ?>> mappers;
    private final Map<Class<?>, String> endpointMap;
    private final CircuitBreakerRegistry circuitBreakerRegistry;
    private final RetryRegistry retryRegistry;

    /**
     * WebClientAdapterのコンストラクタ。
     * @Qualifier("externalApiEndpointMap")で、ExternalApiEndpointConfig.javaで定義したBeanを取得するため、
     * 他クラスでは使用している@AllArgsConstructorを使えず、コンストラクタを使用する。
     * 
     * @param webClient WebClient   
     * @param mappers レスポンスマッパーのリスト
     * @param endpointMap エンドポイントマッピング
     * @param circuitBreakerRegistry サーキットブレーカーレジストリ
     * @param retryRegistry リトライレジストリ
     */
    public WebClientAdapter(WebClient webClient, List<ResponseMapper<?, ?>> mappers,
            @Qualifier("externalApiEndpointMap") Map<Class<?>, String> endpointMap, CircuitBreakerRegistry circuitBreakerRegistry, RetryRegistry retryRegistry) {
        this.webClient = webClient;
        this.mappers = mappers;
        this.endpointMap = endpointMap;
        this.circuitBreakerRegistry = circuitBreakerRegistry;
        this.retryRegistry = retryRegistry;
    }

    @Override
    /*
     * 同期呼び出し
     * WebClientでblock()を行い<R>を返すことで、
     * アプリケーション層にアダプター層の詳細を隠蔽し層間の責任分担を明確にする。
     */
    public <T, R> R fetchDataSync(Class<R> domainType) {
        ResponseMapper<T, R> mapper = findMapper(domainType);
        CircuitBreaker circuitBreaker = findCircuitBreaker(domainType);
        Retry retry = findRetry(domainType);
        final String endpoint = endpointMap.get(domainType);

        // リトライとサーキットブレーカーの設定はapplication.ymlで行う
        try {
            return Retry.decorateSupplier(retry, () -> 
                circuitBreaker.executeSupplier(() -> 
                    webClient.get()
                        .uri(endpoint)
                        .retrieve()
                        .bodyToMono((Class<T>)mapper.getResponseType())
                        .map(mapper::mapResponse)
                        .block()
                )
            ).get();
        } catch (WebClientResponseException e) {
            // HTTPステータスコードがエラー（4xx, 5xx）である場合
            if (e.getStatusCode().is4xxClientError()) {
                return handleError(domainType, e, ErrorCodes.WEB_CLIENT_ERROR_400_TO_499);
            } else if (e.getStatusCode().is5xxServerError()) {
                return handleError(domainType, e, ErrorCodes.WEB_CLIENT_ERROR_500_TO_599);
            } else {
                return handleError(domainType, e, ErrorCodes.WEB_CLIENT_ERROR_OTHER);
            }
        } catch (CallNotPermittedException e) {
            // サーキットブレーカーが開いているため、呼び出しが許可されなかった場合の処理
            return handleError(domainType, e, ErrorCodes.WEB_CLIENT_ERROR_CIRCUIT_BREAKER_OPEN);
        } catch (RuntimeException e) {
            // その他の例外の場合
            return handleError(domainType, e, ErrorCodes.WEB_CLIENT_ERROR_OTHER);
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
        String circuitBreakerName = domainType.getSimpleName() + "CircuitBreaker";
        return circuitBreakerRegistry.circuitBreaker(circuitBreakerName);
    }

    private <R> Retry findRetry(Class<R> domainType) {
        // domainTypeに基づいてリトライを選択
        String retryName = domainType.getSimpleName() + "Retry";
        return retryRegistry.retry(retryName);
    }

    private <R> R handleError(Class<R> domainType, RuntimeException e, String errorCode) {
        // TODO：ロギング処理を実装する
        throw new WebClientException(errorCode, e.getMessage() + " for " + domainType.getSimpleName());
    }
}
