package com.example.procdpchallenger.domain.exam.valueobject;

import com.example.procdpchallenger.domain.exception.DomainRuleViolationException;
import com.example.procdpchallenger.shared.exception.ErrorCategory;

import lombok.Getter;

import java.util.Objects;

/*
以下の理由によりrecordではなく、クラスとして実装している。
- 本アプリケーションではDBにnullを保存しないため、valueがnullの場合は空文字を設定する。
- recordにした場合は、コンストラクタではvalueの変更ができない。
- recordのファクトリメソッドでvalueを変更することはできるが、recordはコンストラクタをprivateにできないため、
  recordの生成ルートが2種類になり、コードの可読性・保守性が低下する。
 */
@Getter
public class ExamDescription {
    public static final int MAX_LENGTH = 1000;
    private final String value;

    public ExamDescription(String value) {
        this.value = Objects.requireNonNullElse(value, "");
        if (this.value.length() > MAX_LENGTH) {
            throw new DomainRuleViolationException(ErrorCategory.ERROR, "EXAM_DESCRIPTION_TOO_LONG", "ExamDescription must not exceed " + MAX_LENGTH + " characters. value: " + value);
        }
    }
}
