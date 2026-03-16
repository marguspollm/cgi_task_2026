package ee.margus.resto_reserv_app.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public interface ReservationRequestDetails {
    Integer partySize();

    LocalDate date();

    LocalTime time();
}
