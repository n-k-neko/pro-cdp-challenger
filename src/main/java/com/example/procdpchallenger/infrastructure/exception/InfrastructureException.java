package com.example.procdpchallenger.infrastructure.exception;

import com.example.procdpchallenger.shared.exception.BaseException;

public class InfrastructureException extends BaseException {
    public InfrastructureException(String message, String errorCode) {
        super(message, errorCode);
    }
}
