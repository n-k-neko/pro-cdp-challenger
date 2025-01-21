package com.example.procdpchallenger.application.rule.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.example.procdpchallenger.application.exception.BusinessRuleViolationException;
import com.example.procdpchallenger.application.port.outbound.user.UserRepository;
import com.example.procdpchallenger.domain.user.entity.UserForRegistration;
import com.example.procdpchallenger.domain.user.valueobject.EmailAddress;
import com.example.procdpchallenger.domain.user.valueobject.HashedPassword;
import com.example.procdpchallenger.domain.user.valueobject.UserId;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserIdDuplicationRuleTest {

    private UserRepository userRepository;
    private UserIdDuplicationRule userIdDuplicationRule;

    @BeforeEach
    public void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userIdDuplicationRule = new UserIdDuplicationRule(userRepository);
    }

    @Test
    public void testValidateUserIdNotExists() {
        // ユーザーIDが存在しない場合
        when(userRepository.existsByUserId(new UserId("testUser"))).thenReturn(false);

        UserForRegistration user = new UserForRegistration(
            new UserId("testUser"),
            new HashedPassword("password123"),
            new EmailAddress("test@example.com")
        );

        // ユーザーIDが存在しない場合、例外が発生しないことを確認
        assertDoesNotThrow(() -> userIdDuplicationRule.validate(user));
    }

    @Test
    public void testValidateUserIdExists() {
        // ユーザーIDが存在する場合
        when(userRepository.existsByUserId(new UserId("testUser"))).thenReturn(true);

        UserForRegistration user = new UserForRegistration(
            new UserId("testUser"),
            new HashedPassword("password123"),
            new EmailAddress("test@example.com")
        );

        // ユーザーIDが存在する場合、例外が発生することを確認
        assertThrows(BusinessRuleViolationException.class, () -> userIdDuplicationRule.validate(user));
    }
}