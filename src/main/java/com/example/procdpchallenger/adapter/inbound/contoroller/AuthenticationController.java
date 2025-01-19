package com.example.procdpchallenger.adapter.inbound.contoroller;

import com.example.procdpchallenger.adapter.inbound.dto.LoginRequestDto;
import com.example.procdpchallenger.adapter.inbound.dto.LoginResponseDto;
import com.example.procdpchallenger.adapter.inbound.security.TokenProvider;
import com.example.procdpchallenger.domain.user.valueobject.UserId;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @PostMapping(ApiEndpoints.API_AUTH_LOGIN)
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDto loginRequestDto){
        // ユーザー名とパスワードで認証を試行
        /*
            authenticationManager.authenticateの裏側では、登録されたAuthenticationProviderが順番に呼び出される。
            LoginAuthenticationProviderrのsupportsメソッドで、処理対象とするAuthenticationクラスとして、
            UsernamePasswordAuthenticationTokenを対象としているため、
            authenticationManager.authenticateにUsernamePasswordAuthenticationTokenを渡すと、
            LoginAuthenticationProviderが呼び出される。
         */
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.userId(), loginRequestDto.password())
        );

        // 認証成功時にJWTトークンを返す
        final String token = (String) tokenProvider.generateToken(new UserId(loginRequestDto.userId()));
        return ResponseEntity.ok(new LoginResponseDto(token));
    }
}
