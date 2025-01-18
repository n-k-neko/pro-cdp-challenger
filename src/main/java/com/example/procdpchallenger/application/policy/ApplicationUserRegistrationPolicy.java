package com.example.procdpchallenger.application.policy;

import com.example.procdpchallenger.application.rule.user.ApplicationUserRegistrationRule;
import com.example.procdpchallenger.domain.user.entity.UserForRegistration;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ApplicationUserRegistrationPolicy {
    private final List<ApplicationUserRegistrationRule> rules;

    public void validate(UserForRegistration userForRegistration) {
        for (ApplicationUserRegistrationRule rule : rules) {
            rule.validate(userForRegistration);
        }
    }
}
