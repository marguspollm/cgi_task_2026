package ee.margus.resto_reserv_app.service;

import ee.margus.resto_reserv_app.dto.TableDto;
import ee.margus.resto_reserv_app.entity.RestaurantTable;
import ee.margus.resto_reserv_app.repository.ReservationRepository;
import ee.margus.resto_reserv_app.repository.TableRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TableService {
    @Autowired
    private TableRepository tableRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    /**
     * Get all restaurant tables from the database
     * 
     * @return List of all tables with their details
     */
    @Transactional(readOnly = true)
    public List<TableDto> getAllTables() {
        return tableRepository.findAll()
                .stream()
                .map(this::getTableDto)
                .toList();
    }

    /**
     * Saves or updates a list of tables in the database
     * Removes tables not in the provided list
     * and creates/updates tables that are in the list
     * 
     * @param tables List of TableDto objects to save (new tables should have null
     *               ID)
     * @return The saved/updated tables with their assigned IDs
     */
    @Transactional
    public List<TableDto> saveTables(List<TableDto> tables) {
        // Get all tables
        Map<Long, RestaurantTable> dbTables = tableRepository.findAll()
                .stream()
                .collect(Collectors.toMap(RestaurantTable::getId, rt -> rt));

        // Create set of table IDs from request
        Set<Long> dtoIds = tables.stream()
                .map(TableDto::id)
                .filter(id -> id != null && id > 0)
                .collect(Collectors.toSet());

        // Check if there are tables that are missing from request, but are in database
        // and delete them
        List<RestaurantTable> deleteTables = dbTables.values().stream()
                .filter(table -> !dtoIds.contains(table.getId()))
                .collect(Collectors.toList());

        if (!deleteTables.isEmpty()) {
            deleteTables(deleteTables);
        }

        // Create updated tables
        List<RestaurantTable> restaurantTables = tables.stream()
                .map(table -> getRestaurantTable(table, dbTables))
                .toList();

        List<RestaurantTable> savedRTs = tableRepository.saveAll(restaurantTables);

        return savedRTs.stream()
                .map(this::getTableDto)
                .toList();
    }

    /**
     * Deletes a table from the database if it has no active reservations
     * 
     * @param id ID of the table to delete
     * @throws RuntimeException if the table has any active reservations
     */
    @Transactional
    public void delete(Long id) {
        // Check if table has any future reservations
        boolean hasReservation = reservationRepository
                .existsByRestaurantTable_IdAndDateGreaterThanEqual(id, LocalDate.now());
        if (hasReservation)
            throw new RuntimeException("Table is reserved and cannot be deleted - Id: " + id);

        tableRepository.deleteById(id);
    }

    @Transactional
    private void deleteTables(List<RestaurantTable> deleteTables) {
        deleteTables.forEach(restaurantTable -> delete(restaurantTable.getId()));
    }

    private @NonNull RestaurantTable getRestaurantTable(TableDto tableDto, Map<Long, RestaurantTable> dbTables) {
        RestaurantTable rt;

        // Check if a table has id from request and it exists in database mapping
        if (tableDto.id() != null) {
            rt = Optional.ofNullable(dbTables.get(tableDto.id()))
                    .orElseThrow(() -> new RuntimeException("Table doesn't exist"));
        } else {
            rt = new RestaurantTable();
        }

        rt.setCapacity(tableDto.capacity());
        rt.setAttributes(tableDto.attributes());
        rt.setLocationX(tableDto.locationX());
        rt.setLocationY(tableDto.locationY());

        return rt;
    }

    private @NonNull TableDto getTableDto(RestaurantTable table) {
        return new TableDto(
                table.getId(),
                table.getCapacity(),
                table.getAttributes(),
                table.getLocationX(),
                table.getLocationY());
    }
}
