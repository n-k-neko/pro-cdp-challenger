package com.example.procdpchallenger.domain.exception;

import com.example.procdpchallenger.shared.exception.ApplicationException;

public class DomainRuleViolationException extends ApplicationException {
    public DomainRuleViolationException(String errorCode,String errorMessage) {
        super(errorCode, errorMessage);
    }
}
