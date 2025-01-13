package com.example.procdpchallenger.application.rule.user;

import com.example.procdpchallenger.application.port.outbound.user.UserRepository;
import com.example.procdpchallenger.domain.user.entity.UserForRegistration;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EmailDuplicationRule implements UserRegistrationRule {
    private final UserRepository userRepository;

    @Override
    public void validate(UserForRegistration userForRegistration) {
        if (userRepository.existsByEmail(userForRegistration.email())) {
            throw new EmailAlreadyExistsException("Email is already taken");
        }
    }
}
