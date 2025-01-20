package com.example.procdpchallenger.domain.user.valueobject;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class HashedPasswordTest {
    @Test
    void generateHashedPasswordSuccessfully() {
        PlainPassword plainPassword = new PlainPassword("securePass123");
        Argon2PasswordEncoder passwordEncoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();

        HashedPassword hashedPassword = HashedPassword.from(plainPassword, passwordEncoder);

        assertNotNull(hashedPassword);
    }

    @Test
    void throwExceptionWhenPlainPasswordIsNull() {
        assertThrows(IllegalArgumentException.class, 
            () -> HashedPassword.from(null, Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()));
    }

    @Test
    void throwExceptionWhenPasswordEncoderIsNull() {
        PlainPassword plainPassword = new PlainPassword("securePass123");

        assertThrows(IllegalArgumentException.class, () -> HashedPassword.from(plainPassword, null));
    }
}