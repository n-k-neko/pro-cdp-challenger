package com.example.procdpchallenger.adapter.outbound.webclient.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record IssResponse(
        String message,
        @JsonProperty("iss_position")
        IssPosition issPosition,
        long timestamp
) {
    public static record IssPosition(
            String longitude,
            String latitude
    ) {
    }
}
