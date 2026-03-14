package ee.margus.resto_reserv_app.service;

import ee.margus.resto_reserv_app.model.Table;
import ee.margus.resto_reserv_app.repository.TableRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private TableRepository tableRepository;
    @InjectMocks
    private TableService tableService;

    @Test
    void whenGetAllTables_thenReturnListOfAllTables(){
        List<Table> tables = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Table table = new Table();
            tables.add(table);
        }

        when(tableRepository.findAll()).thenReturn(tables);

        assertEquals(5, tableService.getAllTables().size());
    }

}