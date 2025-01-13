package com.example.procdpchallenger.domain.exam.policy;

import com.example.procdpchallenger.domain.exam.valueobject.ExamStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ExamStateTransitionRule {
    Map<ExamStatus, Set<ExamStatus>> allowed;

    {
        allowed = new HashMap<>();
        allowed.put(ExamStatus.PLANNING, Set.of(ExamStatus.STUDYING));
        allowed.put(ExamStatus.STUDYING, Set.of(ExamStatus.PASSED, ExamStatus.FAILED));
        allowed.put(ExamStatus.FAILED, Set.of(ExamStatus.PLANNING, ExamStatus.STUDYING));
        allowed.put(ExamStatus.PASSED, Set.of(ExamStatus.PLANNING, ExamStatus.STUDYING));
    }

    boolean canTransit(ExamStatus from, ExamStatus to) {
        return allowed.get(from).contains(to);
    }
}
