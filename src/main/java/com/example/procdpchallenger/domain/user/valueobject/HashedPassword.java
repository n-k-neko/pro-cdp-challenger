package com.example.procdpchallenger.domain.user.valueobject;

import org.springframework.security.crypto.password.PasswordEncoder;

public record HashedPassword(String value) {
    public HashedPassword {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("HashedPassword must not be null or blank");
        }
    }

    /**
     * ファクトリメソッド: PlainPassword をハッシュ化して HashedPassword を生成
     */
    public static HashedPassword from(PlainPassword plainPassword, PasswordEncoder passwordEncoder) {
        if (plainPassword == null || passwordEncoder == null) {
            throw new IllegalArgumentException("PlainPassword and PasswordEncoder must not be null");
        }
        return new HashedPassword(passwordEncoder.encode(plainPassword.value()));
    }
}
