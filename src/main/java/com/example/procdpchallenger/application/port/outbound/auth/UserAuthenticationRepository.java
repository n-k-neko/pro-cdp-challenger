package com.example.procdpchallenger.application.port.outbound.auth;

import com.example.procdpchallenger.domain.authentication.entity.UserAuthentication;
import com.example.procdpchallenger.domain.user.valueobject.UserId;

import java.util.Optional;

public interface UserAuthenticationRepository {
    Optional<UserAuthentication> findByUserId(UserId userId);
}
