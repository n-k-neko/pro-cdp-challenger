package com.example.procdpchallenger.domain.information.valueobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LatitudeTest {

    @Test
    void constructor_shouldCreateInstance_whenValueIsZero() {
        // Arrange & Act
        Latitude latitude = new Latitude(BigDecimal.ZERO);

        // Assert
        assertThat(latitude.value()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void constructor_shouldCreateInstance_whenValueIsMinimum() {
        // Arrange & Act
        Latitude latitude = new Latitude(BigDecimal.valueOf(-90));

        // Assert
        assertThat(latitude.value()).isEqualTo(BigDecimal.valueOf(-90));
    }

    @Test
    void constructor_shouldCreateInstance_whenValueIsMaximum() {
        // Arrange & Act
        Latitude latitude = new Latitude(BigDecimal.valueOf(90));

        // Assert
        assertThat(latitude.value()).isEqualTo(BigDecimal.valueOf(90));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-90.1, 90.1, -91, 91, Double.MAX_VALUE, Double.MIN_VALUE})
    void constructor_shouldThrowException_whenValueIsOutOfRange(double invalidValue) {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> new Latitude(BigDecimal.valueOf(invalidValue)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Latitude must be between -90 and 90.");
    }

    @Test
    void constructor_shouldThrowException_whenValueIsNull() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> new Latitude(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Latitude must be between -90 and 90.");
    }

    @Test
    void equals_shouldReturnTrue_whenValuesAreEqual() {
        // Arrange
        Latitude latitude1 = new Latitude(BigDecimal.valueOf(45.0));
        Latitude latitude2 = new Latitude(BigDecimal.valueOf(45.0));

        // Act & Assert
        assertThat(latitude1).isEqualTo(latitude2);
    }

    @Test
    void hashCode_shouldBeEqual_whenValuesAreEqual() {
        // Arrange
        Latitude latitude1 = new Latitude(BigDecimal.valueOf(45.0));
        Latitude latitude2 = new Latitude(BigDecimal.valueOf(45.0));

        // Act & Assert
        assertThat(latitude1.hashCode()).isEqualTo(latitude2.hashCode());
    }
} 