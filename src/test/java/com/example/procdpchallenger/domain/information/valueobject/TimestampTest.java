package com.example.procdpchallenger.domain.information.valueobject;

import org.junit.jupiter.api.Test;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TimestampTest {

    @Test
    void constructor_shouldCreateInstance_whenInstantIsValid() {
        // Arrange
        Instant now = Instant.now();

        // Act
        Timestamp timestamp = new Timestamp(now);

        // Assert
        assertThat(timestamp.value()).isEqualTo(now);
    }

    @Test
    void constructor_shouldCreateInstance_whenInstantIsEpoch() {
        // Arrange
        Instant epoch = Instant.EPOCH;

        // Act
        Timestamp timestamp = new Timestamp(epoch);

        // Assert
        assertThat(timestamp.value()).isEqualTo(epoch);
    }

    @Test
    void constructor_shouldThrowException_whenValueIsNull() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> new Timestamp(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Timestamp cannot be null.");
    }

    @Test
    void equals_shouldReturnTrue_whenValuesAreEqual() {
        // Arrange
        Instant instant = Instant.now();
        Timestamp timestamp1 = new Timestamp(instant);
        Timestamp timestamp2 = new Timestamp(instant);

        // Act & Assert
        assertThat(timestamp1).isEqualTo(timestamp2);
    }

    @Test
    void hashCode_shouldBeEqual_whenValuesAreEqual() {
        // Arrange
        Instant instant = Instant.now();
        Timestamp timestamp1 = new Timestamp(instant);
        Timestamp timestamp2 = new Timestamp(instant);

        // Act & Assert
        assertThat(timestamp1.hashCode()).isEqualTo(timestamp2.hashCode());
    }
} 