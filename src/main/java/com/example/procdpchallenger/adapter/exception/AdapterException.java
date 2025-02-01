package com.example.procdpchallenger.adapter.exception;

import com.example.procdpchallenger.shared.exception.BaseException;

public class AdapterException extends BaseException {
    public AdapterException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
