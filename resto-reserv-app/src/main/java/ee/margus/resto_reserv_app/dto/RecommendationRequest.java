package ee.margus.resto_reserv_app.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record RecommendationRequest(
    @Min(1) Integer partySize,
    @NotNull(message = "Date is required!")
    LocalDate date,
    @NotNull(message = "Time is required!")
    LocalTime time,
    UserPreferences userPreferences) implements ReservationRequestDetails {
}
