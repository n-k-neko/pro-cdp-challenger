package com.example.procdpchallenger.domain.user.valueobject;

import com.example.procdpchallenger.domain.exception.DomainRuleViolationException;
import com.example.procdpchallenger.shared.exception.ErrorCodes;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmailAddressTest {

    @Test
    void testValidEmail() {
        String validEmail = "test@example.com";
        EmailAddress emailAddress = new EmailAddress(validEmail);
        assertEquals(validEmail, emailAddress.value());
    }

    @Test
    void testNullEmail() {
        Exception exception = assertThrows(DomainRuleViolationException.class, () -> {
            new EmailAddress(null);
        });
        assertEquals(ErrorCodes.INVALID_EMAIL_ADDRESS, ((DomainRuleViolationException) exception).getErrorCode());
    }

    @Test
    void testEmptyEmail() {
        Exception exception = assertThrows(DomainRuleViolationException.class, () -> {
            new EmailAddress("");
        });
        assertEquals(ErrorCodes.INVALID_EMAIL_ADDRESS, ((DomainRuleViolationException) exception).getErrorCode());
    }

    @Test
    void testInvalidEmailFormat() {
        String invalidEmail = "invalid-email";
        Exception exception = assertThrows(DomainRuleViolationException.class, () -> {
            new EmailAddress(invalidEmail);
        });
        assertEquals(ErrorCodes.INVALID_EMAIL_ADDRESS, ((DomainRuleViolationException) exception).getErrorCode());
    }
}
