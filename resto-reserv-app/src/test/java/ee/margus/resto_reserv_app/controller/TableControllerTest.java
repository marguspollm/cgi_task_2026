package ee.margus.resto_reserv_app.controller;

import ee.margus.resto_reserv_app.dto.TableDto;
import ee.margus.resto_reserv_app.model.TableAttribute;
import ee.margus.resto_reserv_app.service.TableService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TableController.class)
class TableControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TableService tableService;

    @Test
    void getAllTables_thenReturnList() throws Exception {
        TableDto tableDto = new TableDto(1L, 2, Set.of(), 1, 1);

        when(tableService.getAllTables()).thenReturn(List.of(tableDto));

        mockMvc.perform(get("/tables")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(1))
            .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void givenValidTable_whenSaveTables_thenReturnSavedTables() throws Exception {
        List<TableDto> request = List.of(new TableDto(null, 2, Set.of(), 1, 1));
        List<TableDto> response = List.of(new TableDto(1L, 2, Set.of(), 1, 1));

        when(tableService.saveTables(any())).thenReturn(response);

        mockMvc.perform(post("/tables")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(1))
            .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void givenEmptyTable_whenSaveTables_thenReturnEmptyList() throws Exception {
        when(tableService.saveTables(any())).thenReturn(List.of());

        mockMvc.perform(post("/tables")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(List.of())))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(0));
    }

    @Test
    void givenInvalidTable_whenSaveTables_thenReturnsError() throws Exception {
        when(tableService.saveTables(any())).thenThrow(new RuntimeException("Table doesn't exist"));

        mockMvc.perform(post("/tables")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(List.of())))
            .andExpect(status().isInternalServerError());
    }
    
    @Test
    void getTableAttributes() throws Exception{
        mockMvc.perform(get("/table-attributes"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(TableAttribute.values()));
    }
}