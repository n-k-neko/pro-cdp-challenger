package com.example.procdpchallenger.domain.user.valueobject;

import com.example.procdpchallenger.domain.exception.DomainRuleViolationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class UserIdTest {

    @Test
    public void testValidUserId() {
        // 有効なUserId
        assertDoesNotThrow(() -> new UserId("validUser"));
    }

    @Test
    public void testNullUserId() {
        // UserIdがnullの場合
        assertThrows(DomainRuleViolationException.class, () -> new UserId(null));
    }

    @Test
    public void testEmptyUserId() {
        // UserIdが空の場合
        assertThrows(DomainRuleViolationException.class, () -> new UserId(""));
    }

    @Test
    public void testTooShortUserId() {
        // UserIdが短すぎる場合
        assertThrows(DomainRuleViolationException.class, () -> new UserId("usr"));
    }

    @Test
    public void testTooLongUserId() {
        // UserIdが長すぎる場合
        assertThrows(DomainRuleViolationException.class, () -> new UserId("thisUserIdIsWayTooLong"));
    }

    @Test
    public void testProhibitedWordUserId() {
        // 禁止された単語を含むUserId
        assertThrows(DomainRuleViolationException.class, () -> new UserId("root"));
        assertThrows(DomainRuleViolationException.class, () -> new UserId("admin"));
        assertThrows(DomainRuleViolationException.class, () -> new UserId("user"));
    }

    @Test
    public void testProhibitedWordCaseInsensitive() {
        // 大文字小文字を区別しない禁止された単語
        assertThrows(DomainRuleViolationException.class, () -> new UserId("Root"));
        assertThrows(DomainRuleViolationException.class, () -> new UserId("Admin"));
        assertThrows(DomainRuleViolationException.class, () -> new UserId("User"));
    }
}