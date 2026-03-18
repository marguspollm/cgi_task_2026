package ee.margus.resto_reserv_app.controller;

import ee.margus.resto_reserv_app.dto.TableDto;
import ee.margus.resto_reserv_app.model.TableAttribute;
import ee.margus.resto_reserv_app.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing restaurant tables
 * Provides endpoints for retrieving, saving, and deleting tables and
 * getting table attributes
 */
@RestController
public class TableController {
    @Autowired
    private TableService tableService;

    /**
     * GET endpoint to retrieve all tables
     * 
     * @return A list of all restaurant tables with their details
     */
    @GetMapping("tables")
    public List<TableDto> getAllTables() {
        return tableService.getAllTables();
    }

    /**
     * POST endpoint to save/update tables
     * Performs full synchronization - updates existing tables and removes tables
     * not in the list
     * 
     * @param tables List of TableDto objects to save
     * @return Saved or updated tables with their assigned IDs
     */
    @PostMapping("tables")
    public List<TableDto> saveTables(@RequestBody List<TableDto> tables) {
        return tableService.saveTables(tables);
    }

    /**
     * GET endpoint to retrieve all available table attribute types
     * 
     * @return Array of all TableAttribute enum values
     */
    @GetMapping("table-attributes")
    public TableAttribute[] getAttributes() {
        return TableAttribute.values();
    }

    /**
     * DELETE endpoint to delete a table.
     * 
     * @param id ID of the table to delete
     * @return Boolean indicating success
     * @throws RuntimeException if the table has active reservations
     */
    @DeleteMapping("tables")
    public boolean deleteTable(@RequestParam Long id) {
        tableService.delete(id);
        return true;
    }
}
