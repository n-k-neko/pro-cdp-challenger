package com.example.procdpchallenger.domain.user.valueobject;

public record PlainPassword(String value) {
    public static final int MIN_LENGTH = 8;
    public static final int MAX_LENGTH = 20;

    public PlainPassword {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("PlainPassword must not be null or empty");
        }
        if (value.length() < MIN_LENGTH) {
            throw new IllegalArgumentException("PlainPassword must be at least " + MIN_LENGTH + " characters");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("PlainPassword must not exceed " + MAX_LENGTH + " characters");
        }
    }
}
