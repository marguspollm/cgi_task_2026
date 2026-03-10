package ee.margus.resto_reserv_app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    private Long id;
    private LocalDate date;
    private LocalTime time;
    private Table table;
    private int partySize;
    private Customer customer;
}
