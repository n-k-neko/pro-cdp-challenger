package com.example.procdpchallenger.adapter.inbound.controller;

import com.example.procdpchallenger.adapter.inbound.contoroller.AuthenticationController;
import com.example.procdpchallenger.adapter.inbound.dto.LoginRequestDto;
import com.example.procdpchallenger.adapter.inbound.security.TokenProvider;
import com.example.procdpchallenger.domain.user.valueobject.UserId;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.mockito.Mock;
import org.mockito.InjectMocks;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;
import com.example.procdpchallenger.adapter.inbound.contoroller.ApiEndpoints;

@WebMvcTest(AuthenticationController.class)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenProvider tokenProvider;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Test
    void login_shouldReturnToken_whenCredentialsAreValid() throws Exception {
        // Arrange
        String userId = "testUser";
        String password = "testPassword";
        String expectedToken = "dummy.jwt.token";
        LoginRequestDto loginRequest = new LoginRequestDto(userId, password);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(userId, password));
        when(tokenProvider.generateToken(any(UserId.class))).thenReturn(expectedToken);

        // Act & Assert
        mockMvc.perform(post(ApiEndpoints.API_AUTH_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(expectedToken));
    }

    @Test
    void login_shouldReturnUnauthorized_whenCredentialsAreInvalid() throws Exception {
        // Arrange
        String userId = "wrongUser";
        String password = "wrongPassword";
        LoginRequestDto loginRequest = new LoginRequestDto(userId, password);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        // TODO：以下の例外クラスが存在することを知らなかった。独自に作ったクラスを書き換えたほうが良いのか確認する。
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // Act & Assert
        mockMvc.perform(post(ApiEndpoints.API_AUTH_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_shouldReturnBadRequest_whenRequestBodyIsInvalid() throws Exception {
        // Arrange
        LoginRequestDto loginRequest = new LoginRequestDto("", ""); // 空の認証情報

        // Act & Assert
        mockMvc.perform(post(ApiEndpoints.API_AUTH_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());
    }
} 