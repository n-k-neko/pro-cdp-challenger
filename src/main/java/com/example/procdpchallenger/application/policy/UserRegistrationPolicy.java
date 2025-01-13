package com.example.procdpchallenger.application.policy;

import com.example.procdpchallenger.application.rule.user.UserRegistrationRule;
import com.example.procdpchallenger.domain.user.entity.UserForRegistration;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class UserRegistrationPolicy {
    private final List<UserRegistrationRule> rules;

    public void validate(UserForRegistration userForRegistration) {
        for (UserRegistrationRule rule : rules) {
            rule.validate(userForRegistration);
        }
    }
}
