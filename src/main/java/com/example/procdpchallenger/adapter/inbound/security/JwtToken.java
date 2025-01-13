package com.example.procdpchallenger.adapter.inbound.security;

import lombok.Getter;

@Getter
public class JwtToken {
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String ISSUER = "procdpchallenger";
    // 秘密鍵はセキュリティを考慮してJwtTokenProviderで管理します

    private final String value;

    public JwtToken(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Token must not be null or empty");
        }
        this.value = value;
    }

    public static Date calculateExpirationDate(Date now) {
        return new Date(now.getTime() + EXPIRATION_TIME);
    }
}
