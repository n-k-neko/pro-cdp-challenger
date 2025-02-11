package com.example.procdpchallenger.infrastructure.exception;

import com.example.procdpchallenger.shared.exception.BaseException;
import com.example.procdpchallenger.shared.exception.ErrorCategory;


public class InfrastructureException extends BaseException {
    public InfrastructureException(ErrorCategory errorCategory, String errorCode, String errorMessage) {
        super(errorCategory, errorCode, errorMessage);
    }
}
