package com.example.procdpchallenger.adapter.outbound.persistence;

import com.example.procdpchallenger.application.port.outbound.user.UserRegistrationRepository;
import com.example.procdpchallenger.domain.user.entity.UserForRegistration;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
@AllArgsConstructor
public class UserRegistrationRepositoryImpl implements UserRegistrationRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void save(UserForRegistration userForRegistration) {
        // TODO：ここで実装する
    }

    /*
     * ユーザー登録以外のケースで本SQLの実行が想定されない（※）ため、本Repositoryで実装する。
     * ※：運用・管理は別のアプリケーションで実装するため
     */
    @Override
    public long countRegistrationsByDate(LocalDate date) {
        // TODO：ここで実装する
        return 0;
    }

    @Override
    public int countRegistrations() {
        // TODO：ここで実装する
        return 0;
    }

}
