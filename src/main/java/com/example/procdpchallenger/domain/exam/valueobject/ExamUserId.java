package com.example.procdpchallenger.domain.exam.valueobject;

import com.example.procdpchallenger.domain.user.valueobject.UserId;
import com.example.procdpchallenger.domain.exception.DomainRuleViolationException;
import com.example.procdpchallenger.shared.exception.ErrorCategory;

public record ExamUserId(String value) {
    public static final int MAX_LENGTH = UserId.MAX_LENGTH;

    public ExamUserId {
        if (value == null || value.isEmpty()) {
            throw new DomainRuleViolationException(ErrorCategory.ERROR, "EXAM_USER_ID_NULL_OR_EMPTY", "ExamUserId must not be null or empty");
        }
        if (value.length() > MAX_LENGTH) {
            throw new DomainRuleViolationException(ErrorCategory.ERROR, "EXAM_USER_ID_TOO_LONG", "ExamUserId must not exceed " + MAX_LENGTH + " characters. value: " + value);
        }
    }
}
