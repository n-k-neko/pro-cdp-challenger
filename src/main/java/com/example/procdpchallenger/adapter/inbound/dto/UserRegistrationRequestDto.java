package com.example.procdpchallenger.adapter.inbound.dto;

import com.example.procdpchallenger.application.dto.UserRegistrationCommand;
import com.example.procdpchallenger.domain.user.valueobject.PlainPassword;
import com.example.procdpchallenger.domain.user.valueobject.UserId;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegistrationRequestDto(
        @NotBlank(message = "User ID must not be blank")
        // TODO:カスタムアノテーションでUserId値オブジェクトの最大、最小文字数をチェックするように修正するか検討
        @Size(min = 4, max = 15, message = "User ID must be between 4 and 15 characters")
        String userId,

        @NotBlank(message = "Password must not be blank")
        @Size(min = 8, max = 20, message = "Password must be at least 8 characters")
        String password,

        @NotBlank(message = "Email must not be blank")
        @Email(message = "Email must be a valid email address")
        String email
) {
        public UserRegistrationCommand toCommand() {
                return new UserRegistrationCommand(
                        new UserId(userId),
                        new PlainPassword(password),
                        new com.example.procdpchallenger.domain.user.valueobject.Email(email));
        }
}
