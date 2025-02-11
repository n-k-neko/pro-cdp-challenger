package com.example.procdpchallenger.application.exception;

import com.example.procdpchallenger.shared.exception.BaseException;
import com.example.procdpchallenger.shared.exception.ErrorCategory;

public class BusinessRuleViolationException extends BaseException {
    public BusinessRuleViolationException(ErrorCategory errorCategory, String errorCode, String errorMessage) {
        super(errorCategory, errorCode, errorMessage);
    }
}
