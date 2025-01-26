package com.example.procdpchallenger.domain.information.valueobject;

import java.math.BigDecimal;

public record Longitude(BigDecimal value) {
    public Longitude {
        if (value == null || value.compareTo(BigDecimal.valueOf(-180)) < 0 || value.compareTo(BigDecimal.valueOf(180)) > 0) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180.");
        }
    }
}
