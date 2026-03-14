package ee.margus.resto_reserv_app.util;

import ee.margus.resto_reserv_app.dto.RecommendationRequest;
import ee.margus.resto_reserv_app.dto.ReservationRequest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest { ;
    @Test
    void givenValidReservationRequest_whenValidateRequest_thenDoNothing() {
        ReservationRequest request = new ReservationRequest(
            "Test",
            "5555555",
            1L,
            LocalDate.now().plusDays(2),
            LocalTime.now().plusHours(2),
            2);

        assertDoesNotThrow(() -> Validator.validateRequest(request));
    }

    @Test
    void givenValidRecommendRequest_whenValidateRequest_thenDoNothing() {
        RecommendationRequest request = new RecommendationRequest(
            1,
            LocalDate.now().plusDays(2),
            LocalTime.now().plusHours(2),
            null);

        assertDoesNotThrow(() -> Validator.validateRequest(request));
    }

    @Test
    void givenPastDate_whenValidateRequest_thenDoNothing() {
        RecommendationRequest request = new RecommendationRequest(
            1,
            LocalDate.now().minusDays(2),
            LocalTime.now().plusHours(2),
            null);

        Exception ex = assertThrows(RuntimeException.class, () -> Validator.validateRequest(request));
        assertEquals("Reservation date cannot be in the past!", ex.getMessage());
    }

    @Test
    void givenPastTime_whenValidateRequest_thenDoNothing() {
        RecommendationRequest request = new RecommendationRequest(
            1,
            LocalDate.now(),
            LocalTime.now().minusHours(2),
            null);

        Exception ex = assertThrows(RuntimeException.class, () -> Validator.validateRequest(request));
        assertEquals("Reservation time cannot be in the past!", ex.getMessage());
    }

}