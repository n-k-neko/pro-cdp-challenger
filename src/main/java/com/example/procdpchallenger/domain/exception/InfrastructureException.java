package com.example.procdpchallenger.domain.exception;

import com.example.procdpchallenger.shared.exception.ApplicationException;

public class InfrastructureException extends ApplicationException {
    public InfrastructureException(String message, String errorCode) {
        super(message, errorCode);
    }
}
