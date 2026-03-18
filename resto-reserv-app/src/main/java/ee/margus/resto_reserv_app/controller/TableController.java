package ee.margus.resto_reserv_app.controller;

import ee.margus.resto_reserv_app.dto.TableDto;
import ee.margus.resto_reserv_app.model.TableAttribute;
import ee.margus.resto_reserv_app.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TableController {
    @Autowired
    private TableService tableService;

    @GetMapping("tables")
    public List<TableDto> getAllTables() {
        return tableService.getAllTables();
    }

    @PostMapping("tables")
    public List<TableDto> saveTables(@RequestBody List<TableDto> tables) {
        return tableService.saveTables(tables);
    }

    @GetMapping("table-attributes")
    public TableAttribute[] getAttributes() {
        return TableAttribute.values();
    }

    @DeleteMapping("tables")
    public boolean deleteTable(@RequestParam Long id) {
        tableService.delete(id);
        return true;
    }
}
