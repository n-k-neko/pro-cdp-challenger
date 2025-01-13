package com.example.procdpchallenger.domain.user.valueobject;

public record Email(String value) {
    public Email {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Email must not be null or empty");
        }
        if (!value.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            throw new IllegalArgumentException("Email must be a valid email address");
        }
    }
}
