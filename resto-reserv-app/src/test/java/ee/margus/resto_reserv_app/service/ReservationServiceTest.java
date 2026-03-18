package ee.margus.resto_reserv_app.service;

import ee.margus.resto_reserv_app.dto.ReservationRequest;
import ee.margus.resto_reserv_app.dto.ReservationResponse;
import ee.margus.resto_reserv_app.entity.Customer;
import ee.margus.resto_reserv_app.entity.Reservation;
import ee.margus.resto_reserv_app.entity.RestaurantTable;
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
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {
    public static final LocalDate DATE = LocalDate.now().plusDays(2);
    public static final LocalTime TIME = LocalTime.now().plusHours(2).truncatedTo(ChronoUnit.HOURS);
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private TableRepository tableRepository;

    @InjectMocks
    private ReservationService service;

    private static @NonNull Reservation getReservation(RestaurantTable restaurantTable, LocalDate date, LocalTime time, int partySize) {
        Reservation exisitngReservation = new Reservation();
        exisitngReservation.setId(1L);
        exisitngReservation.setRestaurantTable(restaurantTable);
        exisitngReservation.setCustomer(new Customer("Test Tester", "5555555"));
        exisitngReservation.setDate(date);
        exisitngReservation.setTime(time);
        exisitngReservation.setPartySize(partySize);
        return exisitngReservation;
    }

    private static @NonNull RestaurantTable getTable(Long id) {
        return new RestaurantTable(id, 2, null, 10, 10);
    }

    private static @NonNull ReservationRequest getRequest() {
        return new ReservationRequest(
            "Test Tester",
            "5555555",
            1L,
            ReservationServiceTest.DATE,
            ReservationServiceTest.TIME,
            2);
    }

    @Test
    void givenValidReservationRequest_whenCreateReservation_thenSaveReservationAndReturnResponse() {
        ReservationRequest request = getRequest();

        RestaurantTable restaurantTable = getTable(1L);

        Reservation dbReservation = getReservation(restaurantTable, DATE, TIME, request.partySize());

        when(tableRepository.findById(eq(1L))).thenReturn(Optional.of(restaurantTable));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(dbReservation);

        ReservationResponse response = service.create(request);

        assertEquals(1L, response.id());
        assertEquals("Test Tester", response.customerName());
        assertEquals("5555555", response.phoneNumber());
        assertEquals(1L, response.tableId());
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void givenOverlappingReservationRequest_whenCreateReservation_thenThrowException() {
        ReservationRequest request = getRequest();

        RestaurantTable restaurantTable = getTable(1L);

        Reservation exisitngReservation = getReservation(restaurantTable, DATE, TIME, request.partySize());

        when(reservationRepository
            .findByDateAndTimeBetweenAndRestaurantTable_Id(any(), any(), any(), any()))
            .thenReturn(List.of(exisitngReservation));

        Exception ex = assertThrows(RuntimeException.class, () -> service.create(request));
        assertEquals("Table is already booked for this time", ex.getMessage());
        verify(reservationRepository)
            .findByDateAndTimeBetweenAndRestaurantTable_Id(DATE, TIME.minusHours(2), TIME.plusHours(2), 1L);
    }

    @Test
    void givenCurrentDateOrTime_whenGetReservedTables_thenReturnCurrentReservedTableIds() {
        LocalTime timeNow = LocalTime.now();
        RestaurantTable restaurantTable = getTable(1L);
        Reservation reservation = getReservation(restaurantTable, LocalDate.now(), timeNow, 2);

        when(reservationRepository.findByDateAndTimeBetween(any(), any(), any()))
            .thenReturn(List.of(reservation));

        assertEquals(List.of(1L), service.getReservedTables(LocalDate.now(), timeNow));
        verify(reservationRepository).findByDateAndTimeBetween(LocalDate.now(), timeNow.minusHours(2), timeNow.plusHours(2));
    }

    @Test
    void givenDateAndTime_whenGetReservedTables_thenReturnGivenDateTimeReservedTableIds() {
        RestaurantTable restaurantTable1 = getTable(2L);
        RestaurantTable restaurantTable2 = getTable(3L);
        Reservation reservation1 = getReservation(restaurantTable1, DATE, TIME, 2);
        Reservation reservation2 = getReservation(restaurantTable2, DATE, TIME, 2);


        when(reservationRepository.findByDateAndTimeBetween(any(), any(), any()))
            .thenReturn(List.of(reservation1, reservation2));

        assertEquals(List.of(2L, 3L), service.getReservedTables(DATE, TIME));
        verify(reservationRepository).findByDateAndTimeBetween(DATE, TIME.minusHours(2), TIME.plusHours(2));
    }
}