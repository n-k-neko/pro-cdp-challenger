package com.example.procdpchallenger.adapter.inbound.contoroller;

import com.example.procdpchallenger.adapter.inbound.dto.LoginRequestDto;
import com.example.procdpchallenger.adapter.inbound.dto.LoginResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;

    @PostMapping(ApiEndpoints.API_AUTH_LOGIN)
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDto loginRequestDto){
        // ユーザー名とパスワードで認証を試行
        /*
            authenticationManager.authenticateの裏側では、登録されたAuthenticationProviderが順番に呼び出される。
            JwtAuthenticationProviderのsupportsメソッドで、処理対象とするAuthenticationクラスとして、
            UsernamePasswordAuthenticationTokenを処理対象としているため、
            authenticationManager.authenticateにUsernamePasswordAuthenticationTokenを渡すと、
            JwtAuthenticationProviderが呼び出される。
         */
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.userId(), loginRequestDto.password())
        );

        // 認証成功時にJWTトークンを返す
        String token = (String) authentication.getCredentials();
        return ResponseEntity.ok(new LoginResponseDto(token));
    }
}
