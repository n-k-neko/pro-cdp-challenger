package com.example.procdpchallenger.domain.exam.valueobject;

import com.example.procdpchallenger.domain.exception.DomainRuleViolationException;
import com.example.procdpchallenger.shared.exception.ErrorCategory;

public record ExamName(String value) {
    public static final int MAX_LENGTH = 100;

    public ExamName {
        if (value == null || value.isEmpty()) {
            throw new DomainRuleViolationException(ErrorCategory.ERROR, "EXAM_NAME_NULL_OR_EMPTY", "ExamName must not be null or empty");
        }
        if (value.length() > MAX_LENGTH) {
            throw new DomainRuleViolationException(ErrorCategory.ERROR, "EXAM_NAME_TOO_LONG", "ExamName must not exceed " + MAX_LENGTH + " characters. value: " + value);
        }
    }
}
