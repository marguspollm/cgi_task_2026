package ee.margus.resto_reserv_app.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationFilters(
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate date,
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    LocalTime time,
    String customerName) {
}
