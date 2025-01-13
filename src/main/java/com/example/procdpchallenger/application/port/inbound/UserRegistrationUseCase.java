package com.example.procdpchallenger.application.port.inbound;

import com.example.procdpchallenger.application.dto.UserRegistrationCommand;

public interface UserRegistrationUseCase {
    void execute(UserRegistrationCommand command);
}
