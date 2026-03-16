package ee.margus.resto_reserv_app.service;

import ee.margus.resto_reserv_app.entity.RestaurantTable;
import ee.margus.resto_reserv_app.repository.TableRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantTableServiceTest {
    @Mock
    private TableRepository tableRepository;
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

}