package com.example.procdpchallenger.application.port.outbound.user;

import com.example.procdpchallenger.domain.user.valueobject.Email;
import com.example.procdpchallenger.domain.user.valueobject.UserId;

/**
 * UserRepository
 * Userについての汎用的な操作を提供する
 */
public interface UserRepository {
    boolean existsByUserId(UserId userId);
    boolean existsByEmail(Email email);
}
