package com.example.procdpchallenger.domain.user.policy;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import com.example.procdpchallenger.domain.user.entity.UserForRegistration;
import com.example.procdpchallenger.domain.user.rule.DomainUserRegistrationRule;

@Component
@AllArgsConstructor
public class DomainUserRegistrationPolicy {
    private final List<DomainUserRegistrationRule> rules;
    
    public void validate(UserForRegistration userForRegistration) {
        for(DomainUserRegistrationRule rule : rules) {
            rule.validate(userForRegistration);
        }
    }
} 