package ee.margus.resto_reserv_app.repository;

import ee.margus.resto_reserv_app.model.Table;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TableRepository {
    private final List<Table> tables = new ArrayList<>();

    public List<Table> findAll() {
        return tables;
    }

    public Table findById(Long id) {
        return tables.stream()
            .filter(t -> t.getId().equals(id))
            .findFirst().orElseThrow(() -> new RuntimeException("Table not found!"));
    }

    public void save(Table table){
        tables.add(table);
    }
}
