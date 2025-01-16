package com.example.procdpchallenger.application.exception;

import com.example.procdpchallenger.shared.exception.ApplicationException;

public class BusinessRuleViolationException extends ApplicationException {
    public BusinessRuleViolationException(String message, String errorCode) {
        super(message, errorCode);
    }
}
