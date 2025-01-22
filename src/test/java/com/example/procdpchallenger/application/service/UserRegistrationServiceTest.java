package com.example.procdpchallenger.application.service;

import com.example.procdpchallenger.application.dto.UserRegistrationCommand;
import com.example.procdpchallenger.application.policy.ApplicationUserRegistrationPolicy;
import com.example.procdpchallenger.application.port.outbound.user.UserRegistrationRepository;
import com.example.procdpchallenger.domain.user.entity.UserForRegistration;
import com.example.procdpchallenger.domain.user.policy.DomainUserRegistrationPolicy;
import com.example.procdpchallenger.domain.user.valueobject.EmailAddress;
import com.example.procdpchallenger.domain.user.valueobject.PlainPassword;
import com.example.procdpchallenger.domain.user.valueobject.UserId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRegistrationServiceTest {

    @Mock
    private UserRegistrationRepository userRegistrationRepository;

    @Mock
    private DomainUserRegistrationPolicy domainUserRegistrationPolicy;

    @Mock
    private ApplicationUserRegistrationPolicy applicationUserRegistrationPolicy;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserRegistrationService userRegistrationService;

    @BeforeEach
    void setUp() {
        userRegistrationService = new UserRegistrationService(
            userRegistrationRepository,
            domainUserRegistrationPolicy,
            applicationUserRegistrationPolicy,
            passwordEncoder
        );
    }

    @Test
    void shouldCompleteUserRegistrationSuccessfully() {
        // Given
        UserRegistrationCommand command = new UserRegistrationCommand(
            new UserId("testUser"),
            new PlainPassword("password123"),
            new EmailAddress("test@example.com")
        );
        when(passwordEncoder.encode(any())).thenReturn("hashedPassword");

        // When
        userRegistrationService.execute(command);

        // Then
        verify(domainUserRegistrationPolicy).validate(any(UserForRegistration.class));
        verify(applicationUserRegistrationPolicy).validate(any(UserForRegistration.class));
        verify(userRegistrationRepository).save(any(UserForRegistration.class));
    }

    @Test
    void shouldThrowExceptionWhenDomainPolicyViolated() {
        // Given
        UserRegistrationCommand command = new UserRegistrationCommand(
            new UserId("testUser"),
            new PlainPassword("password123"),
            new EmailAddress("test@example.com")
        );
        doThrow(new IllegalArgumentException("ドメインポリシー違反"))
            .when(domainUserRegistrationPolicy).validate(any(UserForRegistration.class));

        // When/Then
        org.junit.jupiter.api.Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> userRegistrationService.execute(command)
        );
        verify(userRegistrationRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenApplicationPolicyViolated() {
        // Given
        UserRegistrationCommand command = new UserRegistrationCommand(
            new UserId("testUser"),
            new PlainPassword("password123"),
            new EmailAddress("test@example.com")
        );
        doThrow(new IllegalArgumentException("アプリケーションポリシー違反"))
            .when(applicationUserRegistrationPolicy).validate(any(UserForRegistration.class));

        // When/Then
        org.junit.jupiter.api.Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> userRegistrationService.execute(command)
        );
        verify(userRegistrationRepository, never()).save(any());
    }
} 