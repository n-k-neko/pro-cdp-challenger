package com.example.procdpchallenger.domain.user.valueobject;

import com.example.procdpchallenger.domain.exception.DomainRuleViolationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class PlainPasswordTest {

    @Test
    public void testValidPassword() {
        // 有効なパスワード
        assertDoesNotThrow(() -> new PlainPassword("Valid1!"));
    }

    @Test
    public void testNullPassword() {
        // パスワードがnullの場合
        assertThrows(DomainRuleViolationException.class, () -> new PlainPassword(null));
    }

    @Test
    public void testEmptyPassword() {
        // パスワードが空の場合
        assertThrows(DomainRuleViolationException.class, () -> new PlainPassword(""));
    }

    @Test
    public void testShortPassword() {
        // パスワードが短すぎる場合
        assertThrows(DomainRuleViolationException.class, () -> new PlainPassword("Short1!"));
    }

    @Test
    public void testLongPassword() {
        // パスワードが長すぎる場合
        assertThrows(DomainRuleViolationException.class, () -> new PlainPassword("ThisPasswordIsWayTooLong1!"));
    }

    @Test
    public void testPasswordWithoutUppercase() {
        // 大文字が含まれていない場合
        assertThrows(DomainRuleViolationException.class, () -> new PlainPassword("valid1!"));
    }

    @Test
    public void testPasswordWithoutLowercase() {
        // 小文字が含まれていない場合
        assertThrows(DomainRuleViolationException.class, () -> new PlainPassword("VALID1!"));
    }

    @Test
    public void testPasswordWithoutDigit() {
        // 数字が含まれていない場合
        assertThrows(DomainRuleViolationException.class, () -> new PlainPassword("Valid!"));
    }

    @Test
    public void testPasswordWithoutSpecialCharacter() {
        // 特殊文字が含まれていない場合
        assertThrows(DomainRuleViolationException.class, () -> new PlainPassword("Valid1"));
    }
}