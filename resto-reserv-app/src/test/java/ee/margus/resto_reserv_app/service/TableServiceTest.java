package ee.margus.resto_reserv_app.service;

import ee.margus.resto_reserv_app.dto.TableDto;
import ee.margus.resto_reserv_app.entity.RestaurantTable;
import ee.margus.resto_reserv_app.repository.ReservationRepository;
import ee.margus.resto_reserv_app.repository.TableRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private TableRepository tableRepository;
    @Mock
    private ReservationRepository reservationRepository;
    @InjectMocks
    private TableService tableService;

    @Test
    void whenGetAllTables_thenReturnListOfAllTables() {
        List<RestaurantTable> restaurantTables = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            RestaurantTable restaurantTable = new RestaurantTable();
            restaurantTables.add(restaurantTable);
        }

        when(tableRepository.findAll()).thenReturn(restaurantTables);

        assertEquals(5, tableService.getAllTables().size());
    }

    @Test
    void givenNewAddedTable_whenSaveTables_thenReturnUpdatedTables() {
        RestaurantTable saved = new RestaurantTable();
        saved.setId(1L);
        TableDto dto = new TableDto(null, 2, Set.of(), 100, 100);

        when(tableRepository.findAll()).thenReturn(List.of());
        when(tableRepository.saveAll(any())).thenReturn(List.of(saved));

        List<TableDto> result = tableService.saveTables(List.of(dto));

        assertEquals(1, result.size());
        verify(tableRepository).saveAll(any());
    }

    @Test
    void givenUpdatedTable_whenSaveTables_thenReturnUpdatedTables() {
        RestaurantTable existing = new RestaurantTable();
        existing.setId(1L);
        TableDto dto = new TableDto(1L, 2, Set.of(), 100, 100);

        when(tableRepository.findAll()).thenReturn(List.of(existing));
        when(tableRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(tableRepository.saveAll(any())).thenReturn(List.of(existing));

        tableService.saveTables(List.of(dto));

        assertEquals(2, existing.getCapacity());
        assertEquals(100, existing.getLocationX());
        assertEquals(100, existing.getLocationY());
    }

    @Test
    void givenDeletedTable_whenSaveTables_thenDeleteTable() {
        RestaurantTable dbTable1 = new RestaurantTable();
        RestaurantTable dbTable2 = new RestaurantTable();
        dbTable1.setId(1L);
        dbTable2.setId(2L);
        TableDto dto = new TableDto(1L, 2, Set.of(), 100, 100);

        when(tableRepository.findAll()).thenReturn(List.of(dbTable1, dbTable2));
        when(tableRepository.findById(1L)).thenReturn(Optional.of(dbTable1));
        when(tableRepository.saveAll(any())).thenReturn(List.of(dbTable1));
        when(reservationRepository.existsByRestaurantTable_IdAndDateGreaterThanEqual(any(), any())).thenReturn(false);

        tableService.saveTables(List.of(dto));

        assertEquals(2, dbTable1.getCapacity());
        verify(tableRepository).delete(dbTable2);
    }

    @Test
    void givenNotExistingTable_whenSaveTables_thenThrowException() {
        TableDto dto = new TableDto(1L, 2, Set.of(), 100, 100);

        when(tableRepository.findAll()).thenReturn(List.of());
        when(tableRepository.findById(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(RuntimeException.class, () -> tableService.saveTables(List.of(dto)));
        assertEquals("Table doesn't exist", ex.getMessage());
    }

    @Test
    void givenNoTables_whenSaveTables_thenDeleteAllTables() {
        RestaurantTable dbTable1 = new RestaurantTable();
        RestaurantTable dbTable2 = new RestaurantTable();
        dbTable1.setId(1L);
        dbTable2.setId(2L);

        when(tableRepository.findAll()).thenReturn(List.of(dbTable1, dbTable2));
        when(tableRepository.saveAll(any())).thenReturn(List.of());
        when(reservationRepository.existsByRestaurantTable_IdAndDateGreaterThanEqual(any(), any())).thenReturn(false);

        List<TableDto> result = tableService.saveTables(List.of());

        assertEquals(0, result.size());
        verify(tableRepository).delete(dbTable1);
        verify(tableRepository).delete(dbTable2);
    }

}