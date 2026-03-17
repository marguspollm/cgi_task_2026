package ee.margus.resto_reserv_app.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ee.margus.resto_reserv_app.model.TableAttribute;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

public record RecommendationRequest(
    @Min(1) Integer partySize,
    @NotNull(message = "Date is required!")
    LocalDate date,
    @NotNull(message = "Time is required!")
    LocalTime time,
    Set<TableAttribute> userPreferences) implements ReservationRequestDetails {
}
