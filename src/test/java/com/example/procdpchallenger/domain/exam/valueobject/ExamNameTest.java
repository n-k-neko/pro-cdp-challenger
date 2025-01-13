package com.example.procdpchallenger.domain.exam.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExamNameTest {
    @Test
    void constructorShouldAcceptValidName() {
        String validName = "Valid Exam Name";
        ExamName examName = new ExamName(validName);
        assertEquals(validName, examName.value());
    }

    @Test
    void constructorShouldThrowExceptionForNullValue() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ExamName(null));
        assertEquals("ExamName must not be null or empty", exception.getMessage());
    }

    @Test
    void constructorShouldThrowExceptionForEmptyValue() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ExamName(""));
        assertEquals("ExamName must not be null or empty", exception.getMessage());
    }

    @Test
    void constructorShouldThrowExceptionForTooLongValue() {
        String tooLongName = "a".repeat(101);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ExamName(tooLongName));
        assertEquals("ExamName must not exceed 100 characters", exception.getMessage());
    }
}