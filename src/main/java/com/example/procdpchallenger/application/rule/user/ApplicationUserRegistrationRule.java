package com.example.procdpchallenger.application.rule.user;

import com.example.procdpchallenger.domain.user.entity.UserForRegistration;

public interface ApplicationUserRegistrationRule {
    void validate(UserForRegistration userForRegistration);
}
