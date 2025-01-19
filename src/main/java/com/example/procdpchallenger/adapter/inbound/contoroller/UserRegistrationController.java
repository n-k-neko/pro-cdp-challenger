package com.example.procdpchallenger.adapter.inbound.contoroller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.procdpchallenger.adapter.inbound.dto.UserRegistrationRequestDto;
import com.example.procdpchallenger.adapter.inbound.dto.UserRegistrationResponseDto;
import com.example.procdpchallenger.adapter.inbound.security.TokenProvider;
import com.example.procdpchallenger.application.port.inbound.UserRegistrationUseCase;
import com.example.procdpchallenger.domain.user.valueobject.UserId;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class UserRegistrationController {

    private final UserRegistrationUseCase userRegistrationUseCase;
    private final TokenProvider tokenProvider;

    @PostMapping(ApiEndpoints.API_USER)
    public ResponseEntity<?> register(@RequestBody @Valid UserRegistrationRequestDto userRegistrationRequestDto){
        userRegistrationUseCase.execute(userRegistrationRequestDto.toCommand());

        // JwtTokenを生成
        final String token = (String) tokenProvider.generateToken(new UserId(userRegistrationRequestDto.userId()));

        return ResponseEntity.status(HttpStatus.CREATED).body(new UserRegistrationResponseDto(token));
    }
}
