package com.example.procdpchallenger.domain.exam.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExamIdTest {

    @Test
    void generate() {
        ExamId examId = ExamId.generate();
        assertNotNull(examId);
        assertNotNull(examId.value());
        assertDoesNotThrow(() -> UUID.fromString(examId.value()));
    }

    @Test
    void value() {
        String uuid = UUID.randomUUID().toString();
        ExamId examId = new ExamId(uuid);
        assertEquals(uuid, examId.value());
    }

    @Test
    void constructorShouldThrowExceptionForNullValue() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ExamId(null));
        assertEquals("ExamId must not be null", exception.getMessage());
    }

    @Test
    void constructorShouldThrowExceptionForInvalidUUID() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ExamId("invalid-uuid"));
        assertEquals("ExamId must be a valid UUID", exception.getMessage());
    }
}