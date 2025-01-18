package com.example.procdpchallenger.infrastructure.security;

import com.example.procdpchallenger.application.port.outbound.auth.UserAuthenticationRepository;
import com.example.procdpchallenger.domain.authentication.entity.UserAuthentication;
import com.example.procdpchallenger.domain.user.valueobject.UserId;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
/**
 * JWT認証を処理するためのカスタム認証プロバイダ。
 * ユーザー名とパスワードを検証し、認証成功時にJWTを生成する。
 */
@Component
@AllArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserAuthenticationRepository userAuthenticationRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserId userId = new UserId(authentication.getName());
        String password = authentication.getCredentials().toString();

        // ユーザー認証処理
        UserAuthentication userAuth = userAuthenticationRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(password, userAuth.hashedPassword().value())) {
            throw new RuntimeException("Invalid credentials");
        }

        final String token = jwtTokenProvider.generateToken(userId);

        return new UsernamePasswordAuthenticationToken(userId.value(), token);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
