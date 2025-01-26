package com.example.procdpchallenger.domain.information.valueobject;

import java.time.Instant;

public record Timestamp(Instant value) {
    public Timestamp {
        if (value == null) {
            throw new IllegalArgumentException("Timestamp cannot be null.");
        }
    }
}
