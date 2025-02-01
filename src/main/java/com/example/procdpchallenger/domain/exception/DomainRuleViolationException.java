package com.example.procdpchallenger.domain.exception;

import com.example.procdpchallenger.shared.exception.BaseException;

public class DomainRuleViolationException extends BaseException {
    public DomainRuleViolationException(String errorCode,String errorMessage) {
        super(errorCode, errorMessage);
    }
}
