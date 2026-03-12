package ee.margus.resto_reserv_app.service;

import ee.margus.resto_reserv_app.model.Table;
import ee.margus.resto_reserv_app.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TableService {
    @Autowired
    private TableRepository tableRepository;

    public List<Table> getAllTables(){
        return tableRepository.findAll();
    }
}
