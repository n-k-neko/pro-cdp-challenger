package com.example.procdpchallenger.domain.user.valueobject;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class HashedPasswordTest {
    @Test
    void generateHashedPasswordSuccessfully() {
        PlainPassword plainPassword = new PlainPassword("securePass123");
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        HashedPassword hashedPassword = HashedPassword.from(plainPassword, passwordEncoder);

        assertNotNull(hashedPassword);
    }

    @Test
    void throwExceptionWhenPlainPasswordIsNull() {
        assertThrows(IllegalArgumentException.class, () -> HashedPassword.from(null, new BCryptPasswordEncoder()));
    }

    @Test
    void throwExceptionWhenPasswordEncoderIsNull() {
        PlainPassword plainPassword = new PlainPassword("securePass123");

        assertThrows(IllegalArgumentException.class, () -> HashedPassword.from(plainPassword, null));
    }

}