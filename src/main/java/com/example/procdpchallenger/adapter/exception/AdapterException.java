package com.example.procdpchallenger.adapter.exception;

import com.example.procdpchallenger.shared.exception.BaseException;
import com.example.procdpchallenger.shared.exception.ErrorCategory;

public class AdapterException extends BaseException {
    public AdapterException(ErrorCategory errorCategory, String errorCode, String errorMessage) {
        super(errorCategory, errorCode, errorMessage);
    }
}
