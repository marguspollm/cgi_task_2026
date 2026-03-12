package ee.margus.resto_reserv_app.repository;

import ee.margus.resto_reserv_app.model.Table;
import org.springframework.stereotype.Repository;

import java.util.List;

import static ee.margus.resto_reserv_app.model.TableAttribute.PRIVATE;
import static ee.margus.resto_reserv_app.model.TableAttribute.WINDOW;

@Repository
public class TableRepository {
    private final List<Table> tables;

    public TableRepository() {
        this.tables = loadTables();
    }

    private List<Table> loadTables() {
        return List.of(
            new Table(1L, 2, null, 10, 10),
            new Table(2L, 4, null, 20, 20),
            new Table(3L, 2, WINDOW, 30, 30),
            new Table(4L, 4, WINDOW, 40, 30),
            new Table(5L, 2, PRIVATE, 50, 50),
            new Table(6L, 6, null, 20, 30)
        );
    }

    public List<Table> findAll() {
        return tables;
    }

    public Table findById(Long id) {
        return tables.stream()
            .filter(t -> t.getId().equals(id))
            .findFirst().orElseThrow(() -> new RuntimeException("Table not found!"));
    }
}
