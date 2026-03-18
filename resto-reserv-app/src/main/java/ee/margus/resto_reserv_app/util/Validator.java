package ee.margus.resto_reserv_app.util;

import ee.margus.resto_reserv_app.dto.ReservationRequestDetails;

import java.time.LocalDate;
import java.time.LocalTime;

public class Validator {
    /**
     * Validates request time and date params
     * 
     * @param request Reservation request with details
     * @throws RunTimeException if time or date and time are in the past
     */
    public static void validateRequest(ReservationRequestDetails request) {
        if (request.date().isBefore(LocalDate.now())) {
            throw new RuntimeException("Reservation date cannot be in the past!");
        } else if (request.date().isEqual(LocalDate.now()) && request.time().isBefore(LocalTime.now())) {
            throw new RuntimeException("Reservation time cannot be in the past!");
        }
    }
}
