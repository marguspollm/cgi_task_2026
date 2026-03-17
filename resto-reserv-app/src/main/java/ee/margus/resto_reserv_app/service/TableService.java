package ee.margus.resto_reserv_app.service;

import ee.margus.resto_reserv_app.dto.TableDto;
import ee.margus.resto_reserv_app.entity.RestaurantTable;
import ee.margus.resto_reserv_app.repository.TableRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TableService {
    @Autowired
    private TableRepository tableRepository;

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
            .filter(id -> id != null && id >0)
            .collect(Collectors.toSet());

        List<RestaurantTable> deleteTables = dbTables.stream()
            .filter(table -> !dtoIds.contains(table.getId()))
            .toList();

        if(!deleteTables.isEmpty()){
            tableRepository.deleteAll(deleteTables);
        }

        List<RestaurantTable> restaurantTables = tables.stream()
            .map(this::getRestaurantTable)
            .toList();

        List<RestaurantTable> savedRTs = tableRepository.saveAll(restaurantTables);

        return savedRTs.stream()
            .map(this::getTableDto)
            .toList();
    }

    private @NonNull RestaurantTable getRestaurantTable(TableDto tableDto) {
        RestaurantTable rt;

        if (tableDto.id() != null){
            rt = tableRepository.findById(tableDto.id())
                    .orElseThrow(() -> new RuntimeException("Table doesn't exist"));
        } else {
            rt = new RestaurantTable();
        }

        rt.setCapacity(tableDto.capacity());
        rt.setAttribute(tableDto.attribute());
        rt.setLocationX(tableDto.locationX());
        rt.setLocationY(tableDto.locationY());

        return rt;
    }

    private @NonNull TableDto getTableDto(RestaurantTable table) {
        return new TableDto(
            table.getId(),
            table.getCapacity(),
            table.getAttribute(),
            table.getLocationX(),
            table.getLocationY()
        );
    }
}
