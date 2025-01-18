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
    // インフラ層 

}
