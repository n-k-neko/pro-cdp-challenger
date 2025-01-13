package com.example.procdpchallenger.domain.authentication.entity;

import com.example.procdpchallenger.domain.user.valueobject.HashedPassword;
import com.example.procdpchallenger.domain.user.valueobject.UserId;
import lombok.AllArgsConstructor;
import lombok.Getter;

public record UserAuthentication(UserId userId, HashedPassword hashedPassword) {
}
