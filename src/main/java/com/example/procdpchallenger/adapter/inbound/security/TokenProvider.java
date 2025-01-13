package com.example.procdpchallenger.adapter.inbound.security;

import com.example.procdpchallenger.domain.user.valueobject.UserId;

/**
 * トークンの生成、検証、情報抽出を定義するインターフェース。
 * アダプター層がインフラ層の詳細を隠蔽するために利用する。
 */
public interface TokenProvider {
    String generateToken(UserId userId);
    boolean validateToken(String token);
    String extractUserId(String token);
}
