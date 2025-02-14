package com.example.procdpchallenger.domain.user.valueobject;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.procdpchallenger.domain.exception.DomainRuleViolationException;
import com.example.procdpchallenger.shared.exception.ErrorCategory;

public record HashedPassword(String value) {
    public HashedPassword {
        if (value == null || value.isBlank()) {
            throw new DomainRuleViolationException(ErrorCategory.ERROR, "HASHED_PASSWORD_NULL_OR_BLANK", "HashedPassword must not be null or blank");
        }
    }

    /**
     * ファクトリメソッド: PlainPassword をハッシュ化して HashedPassword を生成
     */
    public static HashedPassword from(PlainPassword plainPassword, PasswordEncoder passwordEncoder) {
        if (plainPassword == null || passwordEncoder == null) {
            throw new DomainRuleViolationException(ErrorCategory.ERROR, "HASHED_PASSWORD_NULL_OR_PASSWORD_ENCODER_NULL", "PlainPassword and PasswordEncoder must not be null");
        }
        return new HashedPassword(passwordEncoder.encode(plainPassword.value()));
    }
}
