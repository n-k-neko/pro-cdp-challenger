package com.example.procdpchallenger.application.rule.user;

import com.example.procdpchallenger.application.exception.BusinessRuleViolationException;
import com.example.procdpchallenger.application.port.outbound.user.UserRepository;
import com.example.procdpchallenger.domain.user.entity.UserForRegistration;
import com.example.procdpchallenger.shared.exception.ErrorCodes;
import com.example.procdpchallenger.shared.exception.ErrorCategory;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserIdDuplicationRule implements ApplicationUserRegistrationRule {
    private final UserRepository userRepository;

    @Override
    public void validate(UserForRegistration userForRegistration) {
        if (userRepository.existsByUserId(userForRegistration.userId())) {
            throw new BusinessRuleViolationException(
                ErrorCategory.INFO,
                ErrorCodes.USER_ALREADY_EXISTS_IN_DB,
                "User ID is already taken");
        }
    }
}
