package com.example.procdpchallenger.adapter.inbound.security;

import java.util.Date;

public class JwtToken {
    public static final String ISSUER = "procdp-challenger";
    public static final long EXPIRATION_TIME = 86400000; // 24時間

    public static Date calculateExpirationDate(Date now) {
        return new Date(now.getTime() + EXPIRATION_TIME);
    }
}
