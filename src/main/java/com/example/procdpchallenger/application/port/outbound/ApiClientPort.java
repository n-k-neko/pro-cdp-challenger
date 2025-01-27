package com.example.procdpchallenger.application.port.outbound;

/**
 * APIクライアントの共通インターフェース。
 */
public interface ApiClientPort {
    // 同期呼び出し
    <T, R> R fetchDataSync(String endpoint, Class<R> domainType);
}
