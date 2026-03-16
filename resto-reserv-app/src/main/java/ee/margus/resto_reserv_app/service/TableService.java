package ee.margus.resto_reserv_app.service;

import ee.margus.resto_reserv_app.dto.TableDto;
import ee.margus.resto_reserv_app.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TableService {
    @Autowired
    private TableRepository tableRepository;

    public List<TableDto> getAllTables(){
        return tableRepository.findAll()
            .stream()
            .map(table ->
                new TableDto(
                    table.getId(),
                    table.getCapacity(),
                    table.getAttribute(),
                    table.getLocationX(),
                    table.getLocationY()
                )
            ).toList();
    }
}
