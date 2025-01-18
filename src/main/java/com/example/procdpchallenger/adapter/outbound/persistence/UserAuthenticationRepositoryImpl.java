package com.example.procdpchallenger.adapter.outbound.persistence;

import com.example.procdpchallenger.application.port.outbound.auth.UserAuthenticationRepository;
import com.example.procdpchallenger.domain.authentication.entity.UserAuthentication;
import com.example.procdpchallenger.domain.user.valueobject.HashedPassword;
import com.example.procdpchallenger.domain.user.valueobject.UserId;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserAuthenticationRepositoryImpl implements UserAuthenticationRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<UserAuthentication> findByUserId(UserId userId) {
        final String sql = """
                SELECT user_id, hashed_password
                FROM users
                WHERE user_id = ?
                """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, 
                (rs, _) -> new UserAuthentication(
                    new UserId(rs.getString("user_id")),
                    new HashedPassword(rs.getString("hashed_password"))
                ),
                userId.value()
            ));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}