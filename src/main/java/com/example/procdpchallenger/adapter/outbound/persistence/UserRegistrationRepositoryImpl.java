package com.example.procdpchallenger.adapter.outbound.persistence;

import com.example.procdpchallenger.application.port.outbound.user.UserRegistrationRepository;
import com.example.procdpchallenger.domain.user.entity.UserForRegistration;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserRegistrationRepositoryImpl implements UserRegistrationRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void save(UserForRegistration userForRegistration) {
        final String sql = """
                INSERT INTO users (user_id, email_address, hashed_password, created_at, updated_at)
                VALUES (?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
                """;
        jdbcTemplate.update(sql, 
            userForRegistration.userId().value(), 
            userForRegistration.emailAddress().value(), 
            userForRegistration.hashedPassword().value()
            );
    }

    /*
     * ユーザー登録以外のケースで本SQLの実行が想定されない（※）ため、本Repositoryで実装する。
     * ※：運用・管理は別のアプリケーションで実装するため
     */
    @Override
    public int countRegistrationsByDate(LocalDate date) {
        final String sql = """
                SELECT COUNT(*)
                FROM users
                WHERE created_at >= ? AND created_at < ?
                """;
        LocalDate nextDay = date.plusDays(1);
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, Integer.class, date, nextDay))
            .orElse(0);
    }

    @Override
    public int countRegistrations() {
        final String sql = """
                SELECT COUNT(*)
                FROM users
                """;
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, Integer.class))
            .orElse(0);
    }
}
