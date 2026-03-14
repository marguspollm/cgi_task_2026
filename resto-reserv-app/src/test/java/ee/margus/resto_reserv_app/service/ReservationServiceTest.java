package ee.margus.resto_reserv_app.service;

import ee.margus.resto_reserv_app.dto.ReservationRequest;
import ee.margus.resto_reserv_app.dto.ReservationResponse;
import ee.margus.resto_reserv_app.model.Customer;
import ee.margus.resto_reserv_app.model.Reservation;
import ee.margus.resto_reserv_app.model.Table;
import ee.margus.resto_reserv_app.repository.ReservationRepository;
import ee.margus.resto_reserv_app.repository.TableRepository;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {
    public static final LocalDate DATE = LocalDate.now().plusDays(2);
    public static final LocalTime TIME = LocalTime.now().plusHours(2);
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private TableRepository tableRepository;

    @InjectMocks
    private ReservationService service;

    private static @NonNull Reservation getReservation(Table table, LocalDate date, LocalTime time, int partySize) {
        Reservation exisitngReservation = new Reservation();
        exisitngReservation.setId(1L);
        exisitngReservation.setTable(table);
        exisitngReservation.setCustomer(new Customer("Test Tester", "5555555"));
        exisitngReservation.setDate(date);
        exisitngReservation.setTime(time);
        exisitngReservation.setPartySize(partySize);
        return exisitngReservation;
    }

    private static @NonNull Table getTable(Long id) {
        return new Table(id, 2, null, 10, 10);
    }

    private static @NonNull ReservationRequest getRequest(LocalDate date, LocalTime time) {
        return new ReservationRequest(
            "Test Tester",
            "5555555",
            1L,
            date,
            time,
            2);
    }

    @Test
    void givenValidReservationRequest_whenCreateReservation_thenSaveReservationAndReturnResponse() {
        ReservationRequest request = getRequest(DATE, TIME);

        Table table = getTable(1L);

        Reservation dbReservation = getReservation(table, DATE, TIME, request.partySize());

        when(tableRepository.findById(1L)).thenReturn(table);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(dbReservation);

        ReservationResponse response = service.create(request);

        assertEquals(1L, response.id());
        assertEquals("Test Tester", response.customerName());
        assertEquals("5555555", response.phoneNumber());
        assertEquals(1L, response.tableId());
    }

    @Test
    void givenOverlapingReservationRequest_whenCreateReservation_thenThrowException() {
        ReservationRequest request = getRequest(DATE, TIME);

        Table table = getTable(1L);

        Reservation exisitngReservation = getReservation(table, DATE, TIME, request.partySize());

        when(reservationRepository.findAll()).thenReturn(List.of(exisitngReservation));

        Exception ex = assertThrows(RuntimeException.class, () -> service.create(request));
        assertEquals("Table is already booked for this time", ex.getMessage());
    }

    @Test
    void givenCurrentDateOrTime_whenGetReservedTables_thenReturnCurrentReservedTableIds() {
        Table table = getTable(1L);
        Reservation reservation = getReservation(table, LocalDate.now(), LocalTime.now(), 2);

        when(reservationRepository.findAll()).thenReturn(List.of(reservation));

        assertEquals(List.of(1L), service.getReservedTables(LocalDate.now(), LocalTime.now()));
    }

    @Test
    void givenDateAndTime_whenGetReservedTables_thenReturnGivenDateTimeReservedTableIds() {
        Table table1 = getTable(1L);
        Table table2 = getTable(2L);
        Table table3 = getTable(3L);
        Reservation reservation1 = getReservation(table1, LocalDate.now(), LocalTime.now(), 2);
        Reservation reservation2 = getReservation(table2, DATE, TIME, 2);
        Reservation reservation3 = getReservation(table3, DATE, TIME, 2);


        when(reservationRepository.findAll()).thenReturn(List.of(reservation1, reservation2, reservation3));

        assertEquals(List.of(2L, 3L), service.getReservedTables(DATE, TIME));

    }
}