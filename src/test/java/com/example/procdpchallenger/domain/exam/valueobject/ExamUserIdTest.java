package com.example.procdpchallenger.domain.exam.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExamUserIdTest {

    @Test
    void testValidExamUserId() {
        String validUserId = "validUserId";
        ExamUserId examUserId = new ExamUserId(validUserId);
        assertEquals(validUserId, examUserId.value());
    }

    @Test
    void testNullExamUserId() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ExamUserId(null));
        assertEquals("ExamUserId must not be null or empty", exception.getMessage());
    }

    @Test
    void testEmptyExamUserId() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ExamUserId(""));
        assertEquals("ExamUserId must not be null or empty", exception.getMessage());
    }

    @Test
    void testTooLongExamUserId() {
        String tooLongUserId = "a".repeat(ExamUserId.MAX_LENGTH + 1);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ExamUserId(tooLongUserId));
        assertEquals("ExamUserId must not exceed " + ExamUserId.MAX_LENGTH + " characters", exception.getMessage());
    }
}