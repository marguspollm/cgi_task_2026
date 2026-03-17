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
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TableService {
    @Autowired
    private TableRepository tableRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    @Transactional(readOnly = true)
    public List<TableDto> getAllTables() {
        return tableRepository.findAll()
            .stream()
            .map(this::getTableDto)
            .toList();
    }

    @Transactional
    public List<TableDto> saveTables(List<TableDto> tables) {
        List<RestaurantTable> dbTables = tableRepository.findAll();

        Set<Long> dtoIds = tables.stream()
            .map(TableDto::id)
            .filter(id -> id != null && id > 0)
            .collect(Collectors.toSet());

        List<RestaurantTable> deleteTables = dbTables.stream()
            .filter(table -> !dtoIds.contains(table.getId()))
            .toList();

        if (!deleteTables.isEmpty()) {
            deleteTables(deleteTables);
        }

        List<RestaurantTable> restaurantTables = tables.stream()
            .map(this::getRestaurantTable)
            .toList();

        List<RestaurantTable> savedRTs = tableRepository.saveAll(restaurantTables);

        return savedRTs.stream()
            .map(this::getTableDto)
            .toList();
    }

    private void deleteTables(List<RestaurantTable> deleteTables) {
        deleteTables.forEach(restaurantTable -> {
            boolean hasReservation = reservationRepository
                .existsByRestaurantTable_IdAndDateGreaterThanEqual(restaurantTable.getId(), LocalDate.now());
            if (hasReservation)
                throw new RuntimeException("Table is reserved and cannot be deleted - Id: " + restaurantTable.getId());
            tableRepository.delete(restaurantTable);
        });
    }

    private @NonNull RestaurantTable getRestaurantTable(TableDto tableDto) {
        RestaurantTable rt;

        if (tableDto.id() != null) {
            rt = tableRepository.findById(tableDto.id())
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
            table.getLocationY()
        );
    }
}
