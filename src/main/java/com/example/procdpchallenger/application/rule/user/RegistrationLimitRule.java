package com.example.procdpchallenger.application.rule.user;

import com.example.procdpchallenger.application.port.outbound.user.UserRegistrationRepository;
import com.example.procdpchallenger.domain.user.entity.UserForRegistration;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RegistrationLimitRule implements UserRegistrationRule {
    private final UserRegistrationRepository userRegistrationRepository;
    public static final int REGISTRATION_LIMIT = 10000;

    @Override
    public void validate(UserForRegistration userForRegistration) {
        if (userRegistrationRepository.countRegistrations() >= REGISTRATION_LIMIT) {
            throw new RuntimeException("Registration limit exceeded");
        }
    }
}