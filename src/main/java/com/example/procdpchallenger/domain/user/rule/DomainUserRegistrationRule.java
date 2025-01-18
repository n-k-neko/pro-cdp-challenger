package com.example.procdpchallenger.domain.user.rule;

import com.example.procdpchallenger.domain.user.entity.UserForRegistration;

public interface DomainUserRegistrationRule {
    void validate(UserForRegistration userForRegistration);
}
