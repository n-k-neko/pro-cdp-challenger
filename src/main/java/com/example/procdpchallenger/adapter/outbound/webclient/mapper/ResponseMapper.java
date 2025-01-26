package com.example.procdpchallenger.adapter.outbound.webclient.mapper;

/*
 * 外部APIのレスポンスをドメインオブジェクトにマッピングするためのインタフェース
 * ドメインオブジェクトは'T'に対応する。レスポンスは'R'に対応する。
 */
public interface ResponseMapper<T, R> {
    R mapResponse(T response);  // 外部APIレスポンスをドメイン型に変換
    Class<T> getResponseType(); // 外部APIレスポンスの型を取得
    Class<R> getDomainType();   // 対応するドメインの型を取得
}