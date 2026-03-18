package ee.margus.resto_reserv_app.dto;

import ee.margus.resto_reserv_app.model.TableAttribute;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

public record RecommendationRequest(
    @Min(1) Integer partySize,
    @NotNull(message = "Date is required!")
    LocalDate date,
    @NotNull(message = "Time is required!")
    LocalTime time,
    Set<TableAttribute> tablePreferences) implements ReservationRequestDetails {
}
