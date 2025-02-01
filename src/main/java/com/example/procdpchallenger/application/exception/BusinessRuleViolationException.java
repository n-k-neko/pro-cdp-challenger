package com.example.procdpchallenger.application.exception;

import com.example.procdpchallenger.shared.exception.BaseException;

public class BusinessRuleViolationException extends BaseException {
    public BusinessRuleViolationException(String errorCode, String message) {
        super(message, errorCode);
    }
}
