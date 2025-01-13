package com.example.procdpchallenger.domain.exam.valueobject;

import java.time.LocalDate;

public record ExamDate(LocalDate value) {
    public static final int MAX_YEARS_IN_FUTURE = 5;
    public ExamDate {
        if (value == null) {
            throw new IllegalArgumentException("ExamDate must not be null");
        }
        if (value.isAfter(LocalDate.now().plusYears(MAX_YEARS_IN_FUTURE))) {
            throw new IllegalArgumentException("ExamDate must not be more than " + MAX_YEARS_IN_FUTURE + " years in the future");
        }
    }
}
