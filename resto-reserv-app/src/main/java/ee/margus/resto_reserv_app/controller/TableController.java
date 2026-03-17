package ee.margus.resto_reserv_app.controller;

import ee.margus.resto_reserv_app.dto.TableDto;
import ee.margus.resto_reserv_app.model.TableAttribute;
import ee.margus.resto_reserv_app.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    public List<TableDto> saveTables(@RequestBody List<TableDto> tables){
        return tableService.saveTables(tables);
    }

    @GetMapping("table-attributes")
    public TableAttribute[] getAttributes() {
        return TableAttribute.values();
    }
}
