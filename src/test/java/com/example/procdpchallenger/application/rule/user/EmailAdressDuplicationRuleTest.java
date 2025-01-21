package com.example.procdpchallenger.application.rule.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.example.procdpchallenger.application.exception.BusinessRuleViolationException;
import com.example.procdpchallenger.application.port.outbound.user.UserRepository;
import com.example.procdpchallenger.domain.user.entity.UserForRegistration;
import com.example.procdpchallenger.domain.user.valueobject.HashedPassword;
import com.example.procdpchallenger.domain.user.valueobject.UserId;
import com.example.procdpchallenger.domain.user.valueobject.EmailAddress;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EmailAdressDuplicationRuleTest {

    private UserRepository userRepository;
    private EmailAdressDuplicationRule emailAdressDuplicationRule;

    @BeforeEach
    public void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        emailAdressDuplicationRule = new EmailAdressDuplicationRule(userRepository);
    }

    @Test
    public void testValidateEmailNotExists() {
        // メールアドレスが存在しない場合
        when(userRepository.existsByEmailAddress(new EmailAddress("test@example.com"))).thenReturn(false);

        UserForRegistration user = new UserForRegistration(
            new UserId("testUser"),
            new HashedPassword("password123"),
            new EmailAddress("test@example.com")
        );

        assertDoesNotThrow(() -> emailAdressDuplicationRule.validate(user));
    }

    @Test
    public void testValidateEmailExists() {
        // メールアドレスが存在する場合
        when(userRepository.existsByEmailAddress(new EmailAddress("test@example.com"))).thenReturn(true);

        UserForRegistration user = new UserForRegistration(
            new UserId("testUser"),
            new HashedPassword("password123"),
            new EmailAddress("test@example.com")
        );

        assertThrows(BusinessRuleViolationException.class, () -> emailAdressDuplicationRule.validate(user));
    }
}