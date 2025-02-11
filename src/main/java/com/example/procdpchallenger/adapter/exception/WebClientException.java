package com.example.procdpchallenger.adapter.exception;

import com.example.procdpchallenger.shared.exception.ErrorCategory;

public class WebClientException extends AdapterException {
    public WebClientException(ErrorCategory errorCategory, String errorCode, String errorMessage) {
        super(errorCategory, errorCode, errorMessage);
    }
}
