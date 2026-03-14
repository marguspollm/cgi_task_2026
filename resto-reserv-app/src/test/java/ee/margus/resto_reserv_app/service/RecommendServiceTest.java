package ee.margus.resto_reserv_app.service;

import ee.margus.resto_reserv_app.dto.RecommendationRequest;
import ee.margus.resto_reserv_app.dto.UserPreferences;
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
import java.util.List;
import java.util.Set;

import static ee.margus.resto_reserv_app.model.TableAttribute.WINDOW;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecommendServiceTest {
    public static final LocalDate DATE = LocalDate.now().plusDays(2);
    public static final LocalTime TIME = LocalTime.now().plusHours(2);
    @Mock
    private TableRepository tableRepository;
    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private RecommendService recommendService;

    @Test
    void givenValidRequestAndTablesNotAvailable_whenGetRecommendedTable_thenThrowException() {
        RecommendationRequest request = new RecommendationRequest(2, DATE, TIME, null);
        Table table = new Table(1L, 2, Set.of(), 10, 10);
        Reservation existingReservation = new Reservation(1L, DATE, TIME, table, 2, new Customer("Test", "55555555"));

        when(reservationRepository.findAll()).thenReturn(List.of(existingReservation));
        when(tableRepository.findAll()).thenReturn(List.of(table));

        Exception ex = assertThrows(RuntimeException.class, () -> recommendService.getRecommendedTable(request));
        assertEquals("No available tables to recommend!", ex.getMessage());
    }

    @Test
    void givenValidRequestAndTablesAvailable_whenGetRecommendedTable_thenReturnRecommendedTableId() {
        UserPreferences preferences = new UserPreferences(null, null, null, null);
        RecommendationRequest request = new RecommendationRequest(2, DATE, TIME, preferences);
        Table table1 = new Table(1L, 2, Set.of(), 10, 10);
        Table table2 = new Table(2L, 2, Set.of(WINDOW), 20, 10);

        when(reservationRepository.findAll()).thenReturn(List.of());
        when(tableRepository.findAll()).thenReturn(List.of(table1, table2));

        assertEquals(1L, recommendService.getRecommendedTable(request));
    }

    @Test
    void givenValidRequestWithPreferencesAndTablesAvailable_whenGetRecommendedTable_thenReturnRecommendedTableId() {
        UserPreferences preferences = new UserPreferences(true, false, false, false);
        RecommendationRequest request = new RecommendationRequest(2, DATE, TIME, preferences);
        Table table1 = new Table(1L, 2, Set.of(), 10, 10);
        Table table2 = new Table(2L, 2, Set.of(WINDOW), 20, 10);

        when(reservationRepository.findAll()).thenReturn(List.of());
        when(tableRepository.findAll()).thenReturn(List.of(table1, table2));

        assertEquals(2L, recommendService.getRecommendedTable(request));
    }

    @Test
    void givenTooSmallTable_whenGetRecommendedTable_thenReturnRecommendedTableId() {
        UserPreferences preferences = new UserPreferences(false, false, false, false);
        RecommendationRequest request = new RecommendationRequest(4, DATE, TIME, preferences);
        Table table1 = new Table(1L, 2, Set.of(), 10, 10);
        Table table2 = new Table(2L, 6, Set.of(WINDOW), 20, 10);

        when(reservationRepository.findAll()).thenReturn(List.of());
        when(tableRepository.findAll()).thenReturn(List.of(table1, table2));

        assertEquals(2L, recommendService.getRecommendedTable(request));
    }

    @Test
    void givenRequestOutsideOfTimeWindow_whenGetRecommendedTable_thenReturnRecommendedTableId() {
        UserPreferences preferences = new UserPreferences(false, false, false, false);
        RecommendationRequest request = new RecommendationRequest(4, DATE, TIME, preferences);
        Table table = new Table(1L, 4, Set.of(), 10, 10);
        Reservation existingReservation = new Reservation(1L, DATE, TIME.minusHours(3), table, 2, new Customer("Test", "55555555"));

        when(reservationRepository.findAll()).thenReturn(List.of(existingReservation));
        when(tableRepository.findAll()).thenReturn(List.of(table));

        assertEquals(1L, recommendService.getRecommendedTable(request));
    }

}