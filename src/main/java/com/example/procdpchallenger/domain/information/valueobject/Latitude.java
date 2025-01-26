package com.example.procdpchallenger.domain.information.valueobject;

import java.math.BigDecimal;

public record Latitude(BigDecimal value) {
    public Latitude {
        if (value == null || value.compareTo(BigDecimal.valueOf(-90)) < 0 || value.compareTo(BigDecimal.valueOf(90)) > 0) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90.");
        }
    }
}
