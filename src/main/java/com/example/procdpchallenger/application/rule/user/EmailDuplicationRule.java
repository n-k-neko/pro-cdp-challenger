package com.example.procdpchallenger.application.rule.user;

import com.example.procdpchallenger.application.exception.BusinessRuleViolationException;
import com.example.procdpchallenger.application.port.outbound.user.UserRepository;
import com.example.procdpchallenger.domain.user.entity.UserForRegistration;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import com.example.procdpchallenger.shared.exception.ErrorCodes;

@Component
@AllArgsConstructor
public class EmailDuplicationRule implements ApplicationUserRegistrationRule {
    private final UserRepository userRepository;

    @Override
    public void validate(UserForRegistration userForRegistration) {
        if (userRepository.existsByEmailAddress(userForRegistration.emailAddress())) {
            throw new BusinessRuleViolationException(
                ErrorCodes.EMAIL_ALREADY_EXISTS_IN_DB,
                "EmailAddress is already taken");
        }
    }
}
