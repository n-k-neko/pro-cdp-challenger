package com.example.procdpchallenger.application.port.outbound;

/**
 * APIクライアントの共通インターフェース。
 * 
 * 外部APIのエンドポイントは、アダプター層でドメインタイプをキーとして管理する。
 * アプリケーション層は、外部サービスのエンドポイントを意識しないでよく、外部への依存を隠蔽する。
 */
public interface ApiClientPort {
    // 同期呼び出し
    <T, R> R fetchDataSync(Class<R> domainType);
}
