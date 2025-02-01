package com.example.procdpchallenger.domain.information.entity;

import com.example.procdpchallenger.domain.information.valueobject.Latitude;
import com.example.procdpchallenger.domain.information.valueobject.Longitude;
import com.example.procdpchallenger.domain.information.valueobject.Timestamp;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IssTest {

    @Test
    void constructor_shouldCreateInstance_whenAllValuesAreValid() {
        // Arrange
        Longitude longitude = new Longitude(BigDecimal.valueOf(135.0));
        Latitude latitude = new Latitude(BigDecimal.valueOf(35.0));
        Timestamp timestamp = new Timestamp(Instant.now());

        // Act
        Iss iss = new Iss(longitude, latitude, timestamp);

        // Assert
        assertThat(iss.longitude()).isEqualTo(longitude);
        assertThat(iss.latitude()).isEqualTo(latitude);
        assertThat(iss.timestamp()).isEqualTo(timestamp);
    }

    @Test
    void constructor_shouldThrowException_whenLongitudeIsNull() {
        // Arrange
        Latitude latitude = new Latitude(BigDecimal.valueOf(35.0));
        Timestamp timestamp = new Timestamp(Instant.now());

        // Act & Assert
        assertThatThrownBy(() -> new Iss(null, latitude, timestamp))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Longitude cannot be null.");
    }

    @Test
    void constructor_shouldThrowException_whenLatitudeIsNull() {
        // Arrange
        Longitude longitude = new Longitude(BigDecimal.valueOf(135.0));
        Timestamp timestamp = new Timestamp(Instant.now());

        // Act & Assert
        assertThatThrownBy(() -> new Iss(longitude, null, timestamp))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Latitude cannot be null.");
    }

    @Test
    void constructor_shouldThrowException_whenTimestampIsNull() {
        // Arrange
        Longitude longitude = new Longitude(BigDecimal.valueOf(135.0));
        Latitude latitude = new Latitude(BigDecimal.valueOf(35.0));

        // Act & Assert
        assertThatThrownBy(() -> new Iss(longitude, latitude, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Timestamp cannot be null.");
    }

    @Test
    void equals_shouldReturnTrue_whenAllValuesAreEqual() {
        // Arrange
        Longitude longitude = new Longitude(BigDecimal.valueOf(135.0));
        Latitude latitude = new Latitude(BigDecimal.valueOf(35.0));
        Instant instant = Instant.now();
        Timestamp timestamp = new Timestamp(instant);

        Iss iss1 = new Iss(longitude, latitude, timestamp);
        Iss iss2 = new Iss(longitude, latitude, timestamp);

        // Act & Assert
        assertThat(iss1).isEqualTo(iss2);
    }

    @Test
    void hashCode_shouldBeEqual_whenAllValuesAreEqual() {
        // Arrange
        Longitude longitude = new Longitude(BigDecimal.valueOf(135.0));
        Latitude latitude = new Latitude(BigDecimal.valueOf(35.0));
        Instant instant = Instant.now();
        Timestamp timestamp = new Timestamp(instant);

        Iss iss1 = new Iss(longitude, latitude, timestamp);
        Iss iss2 = new Iss(longitude, latitude, timestamp);

        // Act & Assert
        assertThat(iss1.hashCode()).isEqualTo(iss2.hashCode());
    }
} 