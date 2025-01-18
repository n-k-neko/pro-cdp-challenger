package com.example.procdpchallenger.domain.user.valueobject;

import java.util.List;

import com.example.procdpchallenger.domain.exception.DomainRuleViolationException;
import com.example.procdpchallenger.shared.exception.ErrorCodes;
public record UserId(String value) {
    public static final int MAX_LENGTH = 15;
    
    // 一部分が大文字の場合もエラーとする。例えば、"Root"や"Admin"もエラーとする。
    private static final List<String> PROHIBITED_WORDS = List.of("root", "admin", "user");

    public UserId {
        if (value == null || value.isEmpty()) {
            throw new DomainRuleViolationException(ErrorCodes.INVALID_USER_ID, "UserId must not be null or empty");
        }
        if (value.length() > MAX_LENGTH) {
            throw new DomainRuleViolationException(ErrorCodes.INVALID_USER_ID, "UserId must not exceed " + MAX_LENGTH + " characters");
        }
        if (isProhibitedWord(value)) {
            throw new DomainRuleViolationException(ErrorCodes.INVALID_USER_ID, "UserId must not be one of the prohibited words: " + PROHIBITED_WORDS);
        }
    }

    private static boolean isProhibitedWord(String value) {
        return PROHIBITED_WORDS.contains(value.toLowerCase());
    }
}
