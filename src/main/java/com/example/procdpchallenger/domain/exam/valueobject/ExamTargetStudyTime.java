package com.example.procdpchallenger.domain.exam.valueobject;

public record ExamTargetStudyTime(int minutes) {
    public ExamTargetStudyTime {
        if (minutes < 0) {
            throw new IllegalArgumentException("ExamTargetStudyTime must not be negative");
        }
    }

    public ExamTargetStudyTime plus(ExamTargetStudyTime other) {
        return new ExamTargetStudyTime(this.minutes + other.minutes);
    }
}
