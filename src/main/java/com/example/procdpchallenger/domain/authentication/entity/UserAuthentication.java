package com.example.procdpchallenger.domain.authentication.entity;

import com.example.procdpchallenger.domain.user.valueobject.HashedPassword;
import com.example.procdpchallenger.domain.user.valueobject.UserId;

public record UserAuthentication(UserId userId, HashedPassword hashedPassword) {
}
