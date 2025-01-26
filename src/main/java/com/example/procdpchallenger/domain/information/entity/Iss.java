package com.example.procdpchallenger.domain.information.entity;

import com.example.procdpchallenger.domain.information.valueobject.Latitude;
import com.example.procdpchallenger.domain.information.valueobject.Longitude;
import com.example.procdpchallenger.domain.information.valueobject.Timestamp;

public record Iss(Longitude longitude, Latitude latitude, Timestamp timestamp) {
    public Iss {
        if (longitude == null) {
            throw new IllegalArgumentException("Longitude cannot be null.");
        }
        if (latitude == null) {
            throw new IllegalArgumentException("Latitude cannot be null.");
        }
        if (timestamp == null) {
            throw new IllegalArgumentException("Timestamp cannot be null.");
        }
    }
}
