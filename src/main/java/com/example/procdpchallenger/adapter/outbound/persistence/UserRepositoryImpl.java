package com.example.procdpchallenger.adapter.outbound.persistence;

import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.example.procdpchallenger.application.port.outbound.user.UserRepository;
import com.example.procdpchallenger.domain.user.valueobject.EmailAddress;
import com.example.procdpchallenger.domain.user.valueobject.UserId;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;
    @Override
    public boolean existsByUserId(UserId userId) {
        final String sql = """
                SELECT COUNT(*)
                FROM users
                WHERE user_id = ?
                """;
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, Integer.class, userId.value()))
            .orElse(0) > 0;
    }

    @Override
    public boolean existsByEmailAddress(EmailAddress emailAddress){
        final String sql = """
                SELECT COUNT(*)
                FROM users
                WHERE email_address = ?
                """;
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, Integer.class, emailAddress.value()))
            .orElse(0) > 0;
    }
}
