package ee.margus.resto_reserv_app.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationRequest(
    @NotBlank(message = "Customer name is required!")
    String customerName,
    @NotBlank(message = "Phone number is required!")
    @Pattern(
        regexp = "^\\+?[0-9\\- ]{7,20}$",
        message = "Invalid phone number format"
    )
    String phoneNumber,
    @NotNull
    Long tableId,
    @NotNull(message = "Date is required!")
    LocalDate date,
    @NotNull(message = "Time is required!")
    LocalTime time,
    @Min(1)
    Integer partySize
) implements ReservationRequestDetails{
}
