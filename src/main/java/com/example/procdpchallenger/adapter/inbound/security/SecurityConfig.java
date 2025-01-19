package com.example.procdpchallenger.adapter.inbound.security;

import com.example.procdpchallenger.adapter.inbound.contoroller.ApiEndpoints;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers(ApiEndpoints.API_AUTH + "/**").permitAll()
                    .requestMatchers(HttpMethod.POST, ApiEndpoints.API_USER).permitAll() // ユーザー登録APIは認証不要
                    // TODO：本番環境の場合はH2コンソールへのアクセスを許可しないようにする
                    .requestMatchers("/h2-console/**").permitAll() // H2コンソールへのアクセスを許可
                    .anyRequest().authenticated()
            )
            .headers(headers -> headers.frameOptions(frameOptionsConfig -> 
                frameOptionsConfig.sameOrigin()
            ))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

