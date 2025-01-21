package com.example.procdpchallenger.infrastructure.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.procdpchallenger.application.port.outbound.auth.UserAuthenticationRepository;
import com.example.procdpchallenger.domain.authentication.entity.UserAuthentication;
import com.example.procdpchallenger.domain.user.valueobject.HashedPassword;
import com.example.procdpchallenger.domain.user.valueobject.UserId;
import com.example.procdpchallenger.infrastructure.exception.InvalidCredentialsException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class LoginAuthenticationProviderTest {

    private PasswordEncoder passwordEncoder;
    private UserAuthenticationRepository userAuthenticationRepository;
    private LoginAuthenticationProvider loginAuthenticationProvider;

    @BeforeEach
    public void setUp() {
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userAuthenticationRepository = Mockito.mock(UserAuthenticationRepository.class);
        loginAuthenticationProvider = new LoginAuthenticationProvider(passwordEncoder, userAuthenticationRepository);
    }

    @Test
    public void testAuthenticateSuccess() {
        // 正しいユーザーIDとパスワード
        UserId userId = new UserId("testUser");
        String rawPassword = "password123";
        String hashedPassword = "hashedPassword123";

        UserAuthentication userAuth = new UserAuthentication(userId, new HashedPassword(hashedPassword));

        when(userAuthenticationRepository.findByUserId(userId)).thenReturn(Optional.of(userAuth));
        when(passwordEncoder.matches(rawPassword, hashedPassword)).thenReturn(true);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userId.value(), rawPassword);

        Authentication result = loginAuthenticationProvider.authenticate(authentication);

        assertTrue(result instanceof UsernamePasswordAuthenticationToken);
        assertTrue(result.isAuthenticated());
    }

    @Test
    public void testAuthenticateInvalidCredentials() {
        // 無効なユーザーIDまたはパスワード
        UserId userId = new UserId("testUser");
        String rawPassword = "wrongPassword";
        String hashedPassword = "hashedPassword123";

        UserAuthentication userAuth = new UserAuthentication(userId, new HashedPassword(hashedPassword));

        when(userAuthenticationRepository.findByUserId(userId)).thenReturn(Optional.of(userAuth));
        when(passwordEncoder.matches(rawPassword, hashedPassword)).thenReturn(false);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userId.value(), rawPassword);

        assertThrows(InvalidCredentialsException.class, () -> loginAuthenticationProvider.authenticate(authentication));
    }

    @Test
    public void testAuthenticateUserNotFound() {
        // ユーザーが見つからない場合
        UserId userId = new UserId("testUser");
        String rawPassword = "password123";

        when(userAuthenticationRepository.findByUserId(userId)).thenReturn(Optional.empty());

        Authentication authentication = new UsernamePasswordAuthenticationToken(userId.value(), rawPassword);

        assertThrows(InvalidCredentialsException.class, () -> loginAuthenticationProvider.authenticate(authentication));
    }

    @Test
    public void testSupports() {
        // サポートする認証クラスの確認
        assertTrue(loginAuthenticationProvider.supports(UsernamePasswordAuthenticationToken.class));
    }
}