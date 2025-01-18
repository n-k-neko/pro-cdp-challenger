package com.example.procdpchallenger.application.service;

import com.example.procdpchallenger.application.dto.UserRegistrationCommand;
import com.example.procdpchallenger.application.policy.ApplicationUserRegistrationPolicy;
import com.example.procdpchallenger.application.port.inbound.UserRegistrationUseCase;
import com.example.procdpchallenger.application.port.outbound.user.UserRegistrationRepository;
import com.example.procdpchallenger.domain.user.entity.UserForRegistration;
import com.example.procdpchallenger.domain.user.policy.DomainUserRegistrationPolicy;
import com.example.procdpchallenger.domain.user.valueobject.HashedPassword;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserRegistrationService implements UserRegistrationUseCase {
    private final UserRegistrationRepository userRegistrationRepository;
    private final DomainUserRegistrationPolicy userValidationPolicy; // ドメイン層
    private final ApplicationUserRegistrationPolicy userRegistrationPolicy; // アプリケーション層
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void execute(UserRegistrationCommand command) {
        UserForRegistration userForRegistration = new UserForRegistration(command.userId(), HashedPassword.from(command.plainPassword(), passwordEncoder), command.emailAddress());
        userValidationPolicy.validate(userForRegistration);
        userRegistrationPolicy.validate(userForRegistration);
        userRegistrationRepository.save(userForRegistration);
    }
}
