package com.example.procdpchallenger.domain.exam.valueobject;

import com.example.procdpchallenger.domain.user.valueobject.UserId;

public record ExamUserId(String value) {
    public static final int MAX_LENGTH = UserId.MAX_LENGTH;

    public ExamUserId {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("ExamUserId must not be null or empty");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("ExamUserId must not exceed " + MAX_LENGTH + " characters");
        }
    }
}
