package com.example.procdpchallenger.domain.exam.valueobject;

import java.util.UUID;

public record ExamId(String value) {
    public ExamId {
        if (value == null) {
            throw new IllegalArgumentException("ExamId must not be null");
        }
        try {
            UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("ExamId must be a valid UUID");
        }
    }

    public static ExamId generate() {
        return new ExamId(UUID.randomUUID().toString());
    }
}
