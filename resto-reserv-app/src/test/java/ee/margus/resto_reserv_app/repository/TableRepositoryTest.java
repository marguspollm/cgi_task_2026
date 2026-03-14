package ee.margus.resto_reserv_app.repository;

import ee.margus.resto_reserv_app.model.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TableRepositoryTest {
    private TableRepository tableRepository;

    @BeforeEach
    void setUp() {
        tableRepository = new TableRepository();
        for (int i = 0; i < 3; i++) {
            Table table = new Table();
            table.setId((long) i);
            table.setCapacity(2);
            tableRepository.save(table);
        }
    }

    @Test
    void whenFindAll_thenReturnAllTable() {
        List<Table> tables = tableRepository.findAll();

        assertEquals(3, tables.size());
    }

    @Test
    void givenExistingTableId_whenFindById_thenReturnTable() {
        Table table = tableRepository.findById(1L);

        assertEquals(1L, table.getId());
        assertEquals(2, table.getCapacity());
    }

    @Test
    void givenNotExistingTableId_whenFindById_thenThrowException() {
        assertThrows(RuntimeException.class, () -> tableRepository.findById(4L));
    }
}