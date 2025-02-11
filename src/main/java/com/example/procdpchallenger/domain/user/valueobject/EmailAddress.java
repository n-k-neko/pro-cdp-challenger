package com.example.procdpchallenger.domain.user.valueobject;

import com.example.procdpchallenger.domain.exception.DomainRuleViolationException;
import com.example.procdpchallenger.shared.exception.ErrorCodes;
import com.example.procdpchallenger.shared.exception.ErrorCategory;

public record EmailAddress(String value) {
    public EmailAddress {
        if (value == null || value.isEmpty()) {
            throw new DomainRuleViolationException(ErrorCategory.ERROR, ErrorCodes.INVALID_EMAIL_ADDRESS, "Email must not be null or empty");
        }
        if (!value.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            throw new DomainRuleViolationException(ErrorCategory.ERROR, ErrorCodes.INVALID_EMAIL_ADDRESS, "Email must be a valid email address");
        }
    }
}
