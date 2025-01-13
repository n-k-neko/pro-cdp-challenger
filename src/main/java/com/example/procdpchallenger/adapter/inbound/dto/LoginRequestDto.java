package com.example.procdpchallenger.adapter.inbound.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record LoginRequestDto(
        @NotNull(message = "UserId must not be null")
        @NotEmpty(message = "UserId must not be empty")
        String userId,
        @NotNull(message = "Password must not be null")
        @NotEmpty(message = "Password must not be empty")
        String password) {}