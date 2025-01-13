package com.example.procdpchallenger.domain.exam.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExamTargetStudyTimeTest {

    @Test
    void testValidExamTargetStudyTime() {
        int validStudyTime = 10;
        ExamTargetStudyTime examTargetStudyTime = new ExamTargetStudyTime(validStudyTime);
        assertEquals(validStudyTime, examTargetStudyTime.minutes());
    }

    @Test
    void testNegativeExamTargetStudyTime() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ExamTargetStudyTime(-1));
        assertEquals("ExamTargetStudyTime must not be negative", exception.getMessage());
    }

    @Test
    void testZeroExamTargetStudyTime() {
        ExamTargetStudyTime examTargetStudyTime = new ExamTargetStudyTime(0);
        assertEquals(0, examTargetStudyTime.minutes());
    }

    @Test
    void testAddExamTargetStudyTime() {
        ExamTargetStudyTime time1 = new ExamTargetStudyTime(10);
        ExamTargetStudyTime time2 = new ExamTargetStudyTime(20);
        ExamTargetStudyTime result = time1.plus(time2);
        assertEquals(30, result.minutes());
    }
}