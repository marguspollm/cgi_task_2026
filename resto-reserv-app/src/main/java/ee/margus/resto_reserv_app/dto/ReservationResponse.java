package ee.margus.resto_reserv_app.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationResponse(Long id,
                                  String customerName,
                                  String phoneNumber,
                                  Long tableId,
                                  LocalDate date,
                                  LocalTime time,
                                  int partySize) {
}
