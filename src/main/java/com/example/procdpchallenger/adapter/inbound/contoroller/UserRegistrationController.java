package com.example.procdpchallenger.adapter.inbound.contoroller;

import com.example.procdpchallenger.adapter.inbound.dto.UserRegistrationRequestDto;
import com.example.procdpchallenger.adapter.inbound.dto.UserRegistrationResponseDto;
import com.example.procdpchallenger.application.port.inbound.UserRegistrationUseCase;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserRegistrationController {

    private final UserRegistrationUseCase userRegistrationUseCase;
    private final AuthenticationManager authenticationManager;

    @PostMapping(ApiEndpoints.API_USER)
    public ResponseEntity<?> register(@RequestBody @Valid UserRegistrationRequestDto userRegistrationRequestDto){
        userRegistrationUseCase.execute(userRegistrationRequestDto.toCommand());

        // JwtTokenを生成
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userRegistrationRequestDto.userId(),
                        userRegistrationRequestDto.password()
                )
        );
        final String token = (String) authentication.getCredentials();

        return ResponseEntity.status(HttpStatus.CREATED).body(new UserRegistrationResponseDto(token));
    }
}
