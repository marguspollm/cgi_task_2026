package ee.margus.resto_reserv_app.service;

import ee.margus.resto_reserv_app.dto.ReservationRequest;
import ee.margus.resto_reserv_app.dto.ReservationResponse;
import ee.margus.resto_reserv_app.model.Customer;
import ee.margus.resto_reserv_app.model.Reservation;
import ee.margus.resto_reserv_app.model.Table;
import ee.margus.resto_reserv_app.repository.ReservationRepository;
import ee.margus.resto_reserv_app.repository.TableRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private TableRepository tableRepository;

    @InjectMocks
    private ReservationService service;

    @Test
    void givenValidReservationRequest_whenCreateReservation_thenSaveReservationAndReturnResponse() {
        ReservationRequest request = new ReservationRequest(
            "Test Tester",
            "5555555",
            1L,
            LocalDate.of(2026, 3, 22),
            LocalTime.of(12, 0),
            2);

        Table table = new Table(1L, 2, null, 10, 10);

        Reservation dbReservation = new Reservation();
        dbReservation.setId(1L);
        dbReservation.setTable(table);
        dbReservation.setCustomer(new Customer("Test Tester", "5555555"));
        dbReservation.setDate(request.date());
        dbReservation.setTime(request.time());
        dbReservation.setPartySize(request.partySize());

        when(tableRepository.findById(1L)).thenReturn(table);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(dbReservation);

        ReservationResponse response = service.create(request);

        assertEquals(1L, response.id());
        assertEquals("Test Tester", response.customerName());
        assertEquals("5555555", response.phoneNumber());
        assertEquals(1L, response.tableId());
    }
}