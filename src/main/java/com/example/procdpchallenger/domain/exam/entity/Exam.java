package com.example.procdpchallenger.domain.exam.entity;

import com.example.procdpchallenger.domain.exam.valueobject.*;

public record Exam(
        ExamId Id,
        ExamName name,
        ExamStatus status,
        ExamDate date,
        ExamTargetStudyTime examTargetStudyTime,
        ExamDescription description,
        ExamUserId userId
) {

}
