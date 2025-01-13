package com.example.procdpchallenger.domain.exam.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExamDescriptionTest {

    @Test
    void testValidExamDescription() {
        String validDescription = "This is a valid exam description.";
        ExamDescription examDescription = new ExamDescription(validDescription);
        assertEquals(validDescription, examDescription.getValue());
    }

    @Test
    void testNullExamDescription() {
        ExamDescription examDescription = new ExamDescription(null);
        assertEquals("", examDescription.getValue());
    }

    @Test
    void testExamDescriptionExceedsMaxLength() {
        String longDescription = "a".repeat(ExamDescription.MAX_LENGTH + 1);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ExamDescription(longDescription));
        assertEquals("ExamDescription must not exceed " + ExamDescription.MAX_LENGTH + " characters", exception.getMessage());
    }
}