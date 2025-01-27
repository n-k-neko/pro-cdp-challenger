package com.example.procdpchallenger.domain.information.valueobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LongitudeTest {

    @Test
    void constructor_shouldCreateInstance_whenValueIsZero() {
        // Arrange & Act
        Longitude longitude = new Longitude(BigDecimal.ZERO);

        // Assert
        assertThat(longitude.value()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void constructor_shouldCreateInstance_whenValueIsMinimum() {
        // Arrange & Act
        Longitude longitude = new Longitude(BigDecimal.valueOf(-180));

        // Assert
        assertThat(longitude.value()).isEqualTo(BigDecimal.valueOf(-180));
    }

    @Test
    void constructor_shouldCreateInstance_whenValueIsMaximum() {
        // Arrange & Act
        Longitude longitude = new Longitude(BigDecimal.valueOf(180));

        // Assert
        assertThat(longitude.value()).isEqualTo(BigDecimal.valueOf(180));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-180.1, 180.1, -181, 181, Double.MAX_VALUE, Double.MIN_VALUE})
    void constructor_shouldThrowException_whenValueIsOutOfRange(double invalidValue) {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> new Longitude(BigDecimal.valueOf(invalidValue)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Longitude must be between -180 and 180.");
    }

    @Test
    void constructor_shouldThrowException_whenValueIsNull() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> new Longitude(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Longitude must be between -180 and 180.");
    }

    @Test
    void equals_shouldReturnTrue_whenValuesAreEqual() {
        // Arrange
        Longitude longitude1 = new Longitude(BigDecimal.valueOf(45.0));
        Longitude longitude2 = new Longitude(BigDecimal.valueOf(45.0));

        // Act & Assert
        assertThat(longitude1).isEqualTo(longitude2);
    }

    @Test
    void hashCode_shouldBeEqual_whenValuesAreEqual() {
        // Arrange
        Longitude longitude1 = new Longitude(BigDecimal.valueOf(45.0));
        Longitude longitude2 = new Longitude(BigDecimal.valueOf(45.0));

        // Act & Assert
        assertThat(longitude1.hashCode()).isEqualTo(longitude2.hashCode());
    }
} 