package com.example.procdpchallenger.shared.exception;

/**
 * 基底例外クラス
 */
public abstract class BaseException extends RuntimeException {
    private final String errorCode;
    private final ErrorCategory errorCategory;

    protected BaseException(ErrorCategory errorCategory, String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorCategory = errorCategory;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public ErrorCategory getErrorCategory() {
        return errorCategory;
    }
}
