package ee.margus.resto_reserv_app.service;

import ee.margus.resto_reserv_app.dto.RecommendationRequest;
import ee.margus.resto_reserv_app.entity.RestaurantTable;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecommendServiceTest {
    public static final LocalDate DATE = LocalDate.now().plusDays(2);
    public static final LocalTime TIME = LocalTime.now().plusHours(2);
    @Mock
    private TableRepository tableRepository;

    @InjectMocks
    private RecommendService recommendService;

    @Test
    void givenValidRequestAndTablesNotAvailable_whenGetRecommendedTable_thenThrowException() {
        RecommendationRequest request =
            new RecommendationRequest(2, DATE, TIME, null);

        when(tableRepository.findAvailableTablesByCapacityAndReservation_DateAndReservation_TimeBetween(anyInt(), any(), any(), any()))
            .thenReturn(List.of());

        Exception ex = assertThrows(RuntimeException.class, () -> recommendService.getRecommendedTable(request));
        assertEquals("No available tables to recommend!", ex.getMessage());
    }

    @Test
    void givenValidRequestAndTablesAvailable_whenGetRecommendedTable_thenReturnRecommendedTableId() {
        RecommendationRequest request = new RecommendationRequest(2, DATE, TIME, Set.of());
        RestaurantTable restaurantTable1 = new RestaurantTable(1L, 2, Set.of(), 10, 10);
        RestaurantTable restaurantTable2 = new RestaurantTable(2L, 2, Set.of(WINDOW), 20, 10);

        when(tableRepository.findAvailableTablesByCapacityAndReservation_DateAndReservation_TimeBetween(anyInt(), any(), any(), any()))
            .thenReturn(List.of(restaurantTable1, restaurantTable2));

        assertEquals(1L, recommendService.getRecommendedTable(request));
    }

    @Test
    void givenValidRequestWithPreferencesAndTablesAvailable_whenGetRecommendedTable_thenReturnRecommendedTableId() {
        RecommendationRequest request = new RecommendationRequest(2, DATE, TIME, Set.of(WINDOW));
        RestaurantTable restaurantTable1 = new RestaurantTable(1L, 2, Set.of(), 10, 10);
        RestaurantTable restaurantTable2 = new RestaurantTable(2L, 2, Set.of(WINDOW), 20, 10);

        when(tableRepository.findAvailableTablesByCapacityAndReservation_DateAndReservation_TimeBetween(anyInt(), any(), any(), any()))
            .thenReturn(List.of(restaurantTable1, restaurantTable2));

        assertEquals(2L, recommendService.getRecommendedTable(request));
    }

    @Test
    void givenTooSmallTable_whenGetRecommendedTable_thenReturnRecommendedTableId() {
        RecommendationRequest request = new RecommendationRequest(4, DATE, TIME, Set.of());
        RestaurantTable restaurantTable1 = new RestaurantTable(1L, 2, Set.of(), 10, 10);
        RestaurantTable restaurantTable2 = new RestaurantTable(2L, 6, Set.of(WINDOW), 20, 10);

        when(tableRepository.findAvailableTablesByCapacityAndReservation_DateAndReservation_TimeBetween(anyInt(), any(), any(), any()))
            .thenReturn(List.of(restaurantTable1, restaurantTable2));

        assertEquals(2L, recommendService.getRecommendedTable(request));
    }

    @Test
    void givenRequestOutsideOfTimeWindow_whenGetRecommendedTable_thenReturnRecommendedTableId() {
        RecommendationRequest request = new RecommendationRequest(4, DATE, TIME, Set.of());
        RestaurantTable restaurantTable = new RestaurantTable(1L, 4, Set.of(), 10, 10);

        when(tableRepository.findAvailableTablesByCapacityAndReservation_DateAndReservation_TimeBetween(anyInt(), any(), any(), any()))
            .thenReturn(List.of(restaurantTable));

        assertEquals(1L, recommendService.getRecommendedTable(request));
    }

}