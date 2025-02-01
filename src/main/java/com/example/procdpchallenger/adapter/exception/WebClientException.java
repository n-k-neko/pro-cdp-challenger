package com.example.procdpchallenger.adapter.exception;

public class WebClientException extends AdapterException {
    public WebClientException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
