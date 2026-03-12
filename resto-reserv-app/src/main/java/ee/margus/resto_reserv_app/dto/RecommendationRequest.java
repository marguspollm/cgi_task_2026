package ee.margus.resto_reserv_app.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record RecommendationRequest(
    @Min(1) int partySize,
    @NotNull
    LocalDate date,
    @NotNull
    LocalTime time,
    UserPreferences userPreferences) {
}
