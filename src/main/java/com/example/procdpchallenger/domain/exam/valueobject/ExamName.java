package com.example.procdpchallenger.domain.exam.valueobject;

public record ExamName(String value) {
    public static final int MAX_LENGTH = 100;

    public ExamName {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("ExamName must not be null or empty");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("ExamName must not exceed " + MAX_LENGTH + " characters");
        }
    }
}
