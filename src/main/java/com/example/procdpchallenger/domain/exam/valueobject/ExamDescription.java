package com.example.procdpchallenger.domain.exam.valueobject;

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
            throw new IllegalArgumentException("ExamDescription must not exceed " + MAX_LENGTH + " characters");
        }
    }
}
