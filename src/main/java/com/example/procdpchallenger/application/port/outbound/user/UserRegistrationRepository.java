package com.example.procdpchallenger.application.port.outbound.user;

import com.example.procdpchallenger.domain.user.entity.UserForRegistration;

import java.time.LocalDate;

public interface UserRegistrationRepository {
    void save(UserForRegistration user);
    long countRegistrationsByDate(LocalDate date);
    int countRegistrations();
}
