package com.example.procdpchallenger.domain.exam.valueobject;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ExamDateTest {

    @Test
    void testValidExamDate() {
        LocalDate validDate = LocalDate.now().plusYears(1);
        ExamDate examDate = new ExamDate(validDate);
        assertEquals(validDate, examDate.value());
    }

    @Test
    void testNullExamDate() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ExamDate(null));
        assertEquals("ExamDate must not be null", exception.getMessage());
    }

    @Test
    void testExamDateTooFarInFuture() {
        LocalDate futureDate = LocalDate.now().plusYears(ExamDate.MAX_YEARS_IN_FUTURE + 1);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ExamDate(futureDate));
        assertEquals("ExamDate must not be more than " + ExamDate.MAX_YEARS_IN_FUTURE + " years in the future", exception.getMessage());
    }
}