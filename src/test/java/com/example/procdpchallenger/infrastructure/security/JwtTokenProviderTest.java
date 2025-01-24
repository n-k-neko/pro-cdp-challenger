package com.example.procdpchallenger.infrastructure.security;

import com.example.procdpchallenger.domain.user.valueobject.UserId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;
    private static final String TEST_SECRET_KEY = "testSecretKeyWithMinimum256BitsForHS256Algorithm";

    @BeforeAll
    static void setUpClass() {
        // テスト用の環境変数を設定
        System.setProperty("JWT_SECRET_KEY", TEST_SECRET_KEY);
    }

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
    }

    @Test
    void generateToken_shouldCreateValidToken_whenValidUserIdProvided() {
        // Arrange
        UserId userId = new UserId("test-user-id");

        // Act
        String token = jwtTokenProvider.generateToken(userId);

        // Assert
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
        assertThat(jwtTokenProvider.validateToken(token)).isTrue();
    }

    @Test
    void validateToken_shouldReturnTrue_whenTokenIsValid() {
        // Arrange
        UserId userId = new UserId("test-user-id");
        String token = jwtTokenProvider.generateToken(userId);

        // Act & Assert
        assertThat(jwtTokenProvider.validateToken(token)).isTrue();
    }

    @Test
    void validateToken_shouldReturnFalse_whenTokenIsInvalid() {
        // Arrange
        String invalidToken = "invalid.token.string";

        // Act & Assert
        assertThat(jwtTokenProvider.validateToken(invalidToken)).isFalse();
    }

    @Test
    void validateToken_shouldReturnFalse_whenTokenIsEmpty() {
        // Act & Assert
        assertThat(jwtTokenProvider.validateToken("")).isFalse();
    }

    @Test
    void extractUserId_shouldExtractCorrectUserId_whenTokenIsValid() {
        // Arrange
        String expectedUserId = "test-user-id";
        UserId userId = new UserId(expectedUserId);
        String token = jwtTokenProvider.generateToken(userId);

        // Act
        String extractedUserId = jwtTokenProvider.extractUserId(token);

        // Assert
        assertThat(extractedUserId).isEqualTo(expectedUserId);
    }

    @Test
    void extractUserId_shouldThrowException_whenTokenIsInvalid() {
        // Arrange
        String invalidToken = "invalid.token.string";

        // Act & Assert
        assertThrows(RuntimeException.class, () -> jwtTokenProvider.extractUserId(invalidToken));
    }
} 