package com.example.procdpchallenger.application.rule.user;

import com.example.procdpchallenger.application.exception.BusinessRuleViolationException;
import com.example.procdpchallenger.application.port.outbound.user.UserRegistrationRepository;
import com.example.procdpchallenger.domain.user.entity.UserForRegistration;
import com.example.procdpchallenger.domain.user.valueobject.EmailAddress;
import com.example.procdpchallenger.domain.user.valueobject.HashedPassword;
import com.example.procdpchallenger.domain.user.valueobject.UserId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RegistrationLimitRuleTest {

    private UserRegistrationRepository userRegistrationRepository;
    private RegistrationLimitRule registrationLimitRule;

    @BeforeEach
    public void setUp() {
        userRegistrationRepository = Mockito.mock(UserRegistrationRepository.class);
        registrationLimitRule = new RegistrationLimitRule(userRegistrationRepository);
    }

    @Test
    public void testValidateWithinLimit() {
        // 登録数が制限内
        when(userRegistrationRepository.countRegistrations()).thenReturn(9999);

        UserForRegistration user = new UserForRegistration(
                new UserId("testUser"),
                new HashedPassword("password123"),
                new EmailAddress("test@example.com")
        );

        // 制限内であれば例外が発生しないことを確認
        assertDoesNotThrow(() -> registrationLimitRule.validate(user));
    }

    @Test
    public void testValidateExceedsLimit() {
        // 登録数が制限を超える
        when(userRegistrationRepository.countRegistrations()).thenReturn(10000);

        UserForRegistration user = new UserForRegistration(
                new UserId("testUser"),
                new HashedPassword("password123"),
                new EmailAddress("test@example.com")
        );

        // 制限を超えた場合に例外が発生することを確認
        assertThrows(BusinessRuleViolationException.class, () -> registrationLimitRule.validate(user));
    }
}