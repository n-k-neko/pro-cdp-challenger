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

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class DailyRegistrationLimitRuleTest {

    private UserRegistrationRepository userRegistrationRepository;
    private DailyRegistrationLimitRule dailyRegistrationLimitRule;
    private Clock fixedClock;

    @BeforeEach
    public void setUp() {
        userRegistrationRepository = Mockito.mock(UserRegistrationRepository.class);
    }

    @Test
    public void testValidateOnWeekdayWithinLimit() {
        // 平日で制限内の登録数
        fixedClock = Clock.fixed(Instant.parse("2023-10-02T00:00:00Z"), ZoneId.of("UTC")); // 月曜日
        dailyRegistrationLimitRule = new DailyRegistrationLimitRule(userRegistrationRepository, fixedClock);

        LocalDate weekday = LocalDate.of(2023, 10, 2);
        when(userRegistrationRepository.countRegistrationsByDate(weekday)).thenReturn(DailyRegistrationLimitRule.MAX_REGISTRATIONS_ON_WEEKDAY - 1);

        UserForRegistration user = new UserForRegistration(
            new UserId("testUser"),
            new HashedPassword("password123"),
            new EmailAddress("test@example.com")
        );

        assertDoesNotThrow(() -> dailyRegistrationLimitRule.validate(user));
    }

    @Test
    public void testValidateOnWeekdayExceedingLimit() {
        // 平日で制限を超えた登録数
        fixedClock = Clock.fixed(Instant.parse("2023-10-02T00:00:00Z"), ZoneId.of("UTC")); // 月曜日
        dailyRegistrationLimitRule = new DailyRegistrationLimitRule(userRegistrationRepository, fixedClock);

        LocalDate weekday = LocalDate.of(2023, 10, 2);
        when(userRegistrationRepository.countRegistrationsByDate(weekday)).thenReturn(DailyRegistrationLimitRule.MAX_REGISTRATIONS_ON_WEEKDAY + 1);

        UserForRegistration user = new UserForRegistration(
            new UserId("testUser"),
            new HashedPassword("password123"),
            new EmailAddress("test@example.com")
        );

        assertThrows(BusinessRuleViolationException.class, () -> dailyRegistrationLimitRule.validate(user));
    }

    @Test
    public void testValidateOnWeekendWithinLimit() {
        // 週末で制限内の登録数
        fixedClock = Clock.fixed(Instant.parse("2023-10-01T00:00:00Z"), ZoneId.of("UTC")); // 日曜日
        dailyRegistrationLimitRule = new DailyRegistrationLimitRule(userRegistrationRepository, fixedClock);

        LocalDate weekend = LocalDate.of(2023, 10, 1);
        when(userRegistrationRepository.countRegistrationsByDate(weekend)).thenReturn(DailyRegistrationLimitRule.MAX_REGISTRATIONS_ON_WEEKEND - 1);

        UserForRegistration user = new UserForRegistration(
            new UserId("testUser"),
            new HashedPassword("password123"),
            new EmailAddress("test@example.com")
        );

        assertDoesNotThrow(() -> dailyRegistrationLimitRule.validate(user));
    }

    @Test
    public void testValidateOnWeekendExceedsLimit() {
        // 週末で制限を超えた登録数
        fixedClock = Clock.fixed(Instant.parse("2023-10-01T00:00:00Z"), ZoneId.of("UTC")); // 日曜日
        dailyRegistrationLimitRule = new DailyRegistrationLimitRule(userRegistrationRepository, fixedClock);

        LocalDate weekend = LocalDate.of(2023, 10, 1);
        when(userRegistrationRepository.countRegistrationsByDate(weekend)).thenReturn(DailyRegistrationLimitRule.MAX_REGISTRATIONS_ON_WEEKEND + 1);

        UserForRegistration user = new UserForRegistration(
            new UserId("testUser"),
            new HashedPassword("password123"),
            new EmailAddress("test@example.com")
        );

        assertThrows(BusinessRuleViolationException.class, () -> dailyRegistrationLimitRule.validate(user));
    }
}