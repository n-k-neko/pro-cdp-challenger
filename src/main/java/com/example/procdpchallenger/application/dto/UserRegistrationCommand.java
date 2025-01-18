package com.example.procdpchallenger.application.dto;

import com.example.procdpchallenger.domain.user.valueobject.EmailAddress;
import com.example.procdpchallenger.domain.user.valueobject.PlainPassword;
import com.example.procdpchallenger.domain.user.valueobject.UserId;

public record UserRegistrationCommand(
        UserId userId,
        PlainPassword plainPassword,
        EmailAddress emailAddress
) {
}
