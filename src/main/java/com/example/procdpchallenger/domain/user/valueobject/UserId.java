package com.example.procdpchallenger.domain.user.valueobject;

public record UserId(String value) {
    public static final int MAX_LENGTH = 15;

    public UserId {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("UserId must not be null or empty");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("UserId must not exceed " + MAX_LENGTH + " characters");
        }
    }
}
