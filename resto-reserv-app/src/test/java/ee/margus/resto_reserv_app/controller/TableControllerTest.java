package ee.margus.resto_reserv_app.controller;

import ee.margus.resto_reserv_app.model.Table;
import ee.margus.resto_reserv_app.service.TableService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TableController.class)
class TableControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TableService tableService;

    @Test
    void getAllTables_thenReturnList() throws Exception{
        Table table = new Table();
        table.setId(1L);
        List<Table> tables = List.of(table);

        when(tableService.getAllTables()).thenReturn(tables);

        mockMvc.perform(get("/tables")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(1))
            .andExpect(jsonPath("$[0].id").value(1));
    }

}