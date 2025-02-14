package com.example.procdpchallenger.domain.exam.valueobject;

import java.time.LocalDate;

import com.example.procdpchallenger.domain.exception.DomainRuleViolationException;
import com.example.procdpchallenger.shared.exception.ErrorCategory;

public record ExamDate(LocalDate value) {
    public static final int MAX_YEARS_IN_FUTURE = 5;
    public ExamDate {
        if (value == null) {
            throw new DomainRuleViolationException(ErrorCategory.ERROR, "EXAM_DATE_NULL", "ExamDate must not be null");
        }
        if (value.isAfter(LocalDate.now().plusYears(MAX_YEARS_IN_FUTURE))) {
            // TODO：入力チェックを行っていないはず。実装する。
            throw new DomainRuleViolationException(ErrorCategory.ERROR, "EXAM_DATE_IN_FUTURE", "ExamDate must not be more than " + MAX_YEARS_IN_FUTURE + " years in the future. value: " + value);
        }
    }
}
