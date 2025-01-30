package com.example.procdpchallenger.adapter.outbound.webclient;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.procdpchallenger.domain.information.entity.Iss;

@Configuration
public class ExternalApiEndpointConfig {

    /**
     * 外部APIのエンドポイントを管理するマッピング。
     * ドメインエンティティをキーとして、エンドポイントを管理する。
     * 今後別の Map<Class<?>, String> 型オブジェクトをDIコンテナに登録する可能性を考慮し、
     * Beanに名前を付与し、コンストラクタがインジェクションするオブジェクトを明確にする。
     * 
     * @return 外部APIのエンドポイントを管理するマッピング
     */
    @Bean(name = "externalApiEndpointMap")
    public Map<Class<?>, String> externalApiEndpointMap() {
        Map<Class<?>, String> endpointMap = new HashMap<>();

        endpointMap.put(Iss.class, "/api/users");

        return endpointMap;
    }
}
