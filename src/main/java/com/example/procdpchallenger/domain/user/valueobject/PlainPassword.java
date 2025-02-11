package com.example.procdpchallenger.domain.user.valueobject;

import com.example.procdpchallenger.domain.exception.DomainRuleViolationException;
import com.example.procdpchallenger.shared.exception.ErrorCodes;
import com.example.procdpchallenger.shared.exception.ErrorCategory;

public record PlainPassword(String value) {
    public static final int MIN_LENGTH = 8;
    public static final int MAX_LENGTH = 20;
    private static final String PASSWORD_REQUIREMENTS = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character";

    public PlainPassword {
        if (value == null || value.isEmpty()) {
            throw new DomainRuleViolationException(ErrorCategory.ERROR, ErrorCodes.INVALID_PASSWORD, "Password must not be null or empty");
        }
        if (value.length() < MIN_LENGTH) {
            throw new DomainRuleViolationException(ErrorCategory.ERROR, ErrorCodes.INVALID_PASSWORD, "Password must be at least " + MIN_LENGTH + " characters");
        }
        if (value.length() > MAX_LENGTH) {
            throw new DomainRuleViolationException(ErrorCategory.ERROR, ErrorCodes.INVALID_PASSWORD, "PlainPassword must not exceed " + MAX_LENGTH + " characters");
        }
        if (!isValidPassword(value)) {
            throw new DomainRuleViolationException(
                ErrorCategory.ERROR, 
                ErrorCodes.INVALID_PASSWORD, 
                PASSWORD_REQUIREMENTS
            );
        }
    }

    private boolean isValidPassword(String password) {
        boolean hasUppercase = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLowercase = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecial = password.chars().anyMatch(ch -> !Character.isLetterOrDigit(ch));

        return hasUppercase && hasLowercase && hasDigit && hasSpecial;
    }
}
