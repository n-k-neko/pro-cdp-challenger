package com.example.procdpchallenger.application.port.outbound;

/**
 * APIクライアントの共通インターフェース。
 * <T> は任意の型を指定でき、レスポンスの型を決定します。
 *
 * 設計意図:
 * - 非同期処理のため、Spring WebFluxのリアクティブ型`Mono<T>`を使用。
 * - `Class<T>`を引数で受け取ることで、ランタイム型情報を指定可能にし、
 *   レスポンスの型安全性を確保。
 */
public interface ApiClientPort {
    <T, R> R fetchData(String endpoint, Class<R> domainType);
}
