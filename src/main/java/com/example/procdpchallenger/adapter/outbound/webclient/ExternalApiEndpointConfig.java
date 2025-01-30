package com.example.procdpchallenger.adapter.outbound.webclient;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.procdpchallenger.domain.information.entity.Iss;

/**
 * 外部APIのエンドポイントを管理するクラス。
 * 外部APIから取得するドメインエンティティをキーとして、エンドポイントを管理する。
 */

@Configuration
public class ExternalApiEndpointConfig {
    @Bean
    public Map<Class<?>, String> externalApiEndpointMap() {
        Map<Class<?>, String> endpointMap = new HashMap<>();

        endpointMap.put(Iss.class, "/api/users");

        return endpointMap;
    }
}
