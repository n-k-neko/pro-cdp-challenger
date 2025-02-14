package com.example.procdpchallenger.domain.exam.valueobject;

import java.util.UUID;

import com.example.procdpchallenger.domain.exception.DomainRuleViolationException;
import com.example.procdpchallenger.shared.exception.ErrorCategory;

public record ExamId(String value) {
    public ExamId {
        if (value == null) {
            throw new DomainRuleViolationException(ErrorCategory.ERROR, "EXAM_ID_NULL", "ExamId must not be null");
        }
        try {
            UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new DomainRuleViolationException(ErrorCategory.ERROR, "EXAM_ID_INVALID", "ExamId must be a valid UUID. value: " + value);
        }
    }

    public static ExamId generate() {
        return new ExamId(UUID.randomUUID().toString());
    }
}
