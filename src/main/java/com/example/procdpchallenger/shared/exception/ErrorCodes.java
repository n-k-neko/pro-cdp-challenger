package com.example.procdpchallenger.shared.exception;

public class ErrorCodes {

    // ドメイン層
    public static final String INVALID_PASSWORD = "DOMAIN_USER_001";
    public static final String INVALID_USER_ID = "DOMAIN_USER_002";
    public static final String INVALID_EMAIL_ADDRESS = "DOMAIN_USER_003";
    // アプリケーション層
    public static final String USER_ALREADY_EXISTS_IN_DB = "APP_USER_001";
    public static final String EMAIL_ALREADY_EXISTS_IN_DB = "APP_USER_002";
    public static final String DAILY_REGISTRATION_LIMIT_EXCEEDED = "APP_USER_003";
    public static final String REGISTRATION_LIMIT_EXCEEDED = "APP_USER_004";
    // アダプター層
    public static final String WEB_CLIENT_ERROR_400_TO_499 = "ADAPTER_WEB_001";
    public static final String WEB_CLIENT_ERROR_500_TO_599 = "ADAPTER_WEB_002";
    public static final String WEB_CLIENT_ERROR_CIRCUIT_BREAKER_OPEN = "ADAPTER_WEB_003";
    public static final String WEB_CLIENT_ERROR_OTHER = "ADAPTER_WEB_004";
    // インフラ層 

}
