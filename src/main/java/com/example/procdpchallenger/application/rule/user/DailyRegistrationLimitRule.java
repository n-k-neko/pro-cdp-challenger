package com.example.procdpchallenger.application.rule.user;

import com.example.procdpchallenger.application.exception.BusinessRuleViolationException;
import com.example.procdpchallenger.application.port.outbound.user.UserRegistrationRepository;
import com.example.procdpchallenger.domain.user.entity.UserForRegistration;
import com.example.procdpchallenger.shared.exception.ErrorCodes;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Component
@AllArgsConstructor
public class DailyRegistrationLimitRule implements ApplicationUserRegistrationRule {
    private final UserRegistrationRepository userRegistrationRepository;
    public final int MAX_REGISTRATIONS_ON_WEEKDAY = 5;
    public final int MAX_REGISTRATIONS_ON_WEEKEND = 10;

    @Override
    public void validate(UserForRegistration userForRegistration){
        final LocalDate today = LocalDate.now();
        final DayOfWeek dayOfWeek = today.getDayOfWeek();

        final int maxRegistrations = isWeekend(dayOfWeek) ? MAX_REGISTRATIONS_ON_WEEKEND : MAX_REGISTRATIONS_ON_WEEKDAY;

        final long registrationsToday = userRegistrationRepository.countRegistrationsByDate(today);
        if(registrationsToday >= maxRegistrations){
            throw new BusinessRuleViolationException(
                    ErrorCodes.DAILY_REGISTRATION_LIMIT_EXCEEDED,
                    String.format("Sorry, Daily registration limit exceeded (%d users allowed today). Try again tomorrow.", maxRegistrations)
            );
        }
    }

    private boolean isWeekend(DayOfWeek dayOfWeek){
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }
}
