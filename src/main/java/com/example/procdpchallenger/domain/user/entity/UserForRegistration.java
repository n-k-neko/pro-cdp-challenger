package com.example.procdpchallenger.domain.user.entity;

import com.example.procdpchallenger.domain.user.valueobject.EmailAddress;
import com.example.procdpchallenger.domain.user.valueobject.HashedPassword;
import com.example.procdpchallenger.domain.user.valueobject.UserId;

public record UserForRegistration(
        UserId userId,
        HashedPassword hashedPassword,
        EmailAddress emailAddress
) {
}
