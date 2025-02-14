package com.example.procdpchallenger.domain.exam.valueobject;

import com.example.procdpchallenger.domain.exception.DomainRuleViolationException;
import com.example.procdpchallenger.shared.exception.ErrorCategory;

public record ExamTargetStudyTime(int minutes) {
    public ExamTargetStudyTime {
        if (minutes < 0) {
            throw new DomainRuleViolationException(ErrorCategory.ERROR, "EXAM_TARGET_STUDY_TIME_NEGATIVE", "ExamTargetStudyTime must not be negative. value: " + minutes);
        }
    }

    public ExamTargetStudyTime plus(ExamTargetStudyTime other) {
        return new ExamTargetStudyTime(this.minutes + other.minutes);
    }
}
