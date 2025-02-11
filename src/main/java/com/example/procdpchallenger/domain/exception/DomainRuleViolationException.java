package com.example.procdpchallenger.domain.exception;

import com.example.procdpchallenger.shared.exception.BaseException;
import com.example.procdpchallenger.shared.exception.ErrorCategory;

public class DomainRuleViolationException extends BaseException {
    public DomainRuleViolationException(ErrorCategory errorCategory, String errorCode, String errorMessage) {
        super(errorCategory, errorCode, errorMessage);
    }
}
