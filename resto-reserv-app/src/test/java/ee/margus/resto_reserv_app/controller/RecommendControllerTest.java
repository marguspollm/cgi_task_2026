package ee.margus.resto_reserv_app.controller;

import ee.margus.resto_reserv_app.dto.RecommendationRequest;
import ee.margus.resto_reserv_app.dto.UserPreferences;
import ee.margus.resto_reserv_app.service.RecommendService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecommendController.class)
class RecommendControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private RecommendService recommendService;

    @Test
    void getRecommendedTable_whenAvailableTables_thenReturnTableId() throws Exception {
        RecommendationRequest request =
            new RecommendationRequest(
                1,
                LocalDate.now(),
                LocalTime.now().plusHours(2),
                new UserPreferences(false, false, false, false)
            );
        when(recommendService.getRecommendedTable(any(RecommendationRequest.class))).thenReturn(1L);

        mockMvc.perform(post("/recommended-table")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string("1"));
    }

    @Test
    void getRecommendedTable_whenNoAvailableTables_thenThrowError() throws Exception {
        RecommendationRequest request =
            new RecommendationRequest(
                1,
                LocalDate.now(),
                LocalTime.now().plusHours(2),
                new UserPreferences(false, false, false, false)
            );
        when(recommendService.getRecommendedTable(any(RecommendationRequest.class)))
            .thenThrow(new RuntimeException("No available tables to recommend!"));

        mockMvc.perform(post("/recommended-table")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());
    }

    @Test
    void getRecommendedTable_whenRequestDateAndTimeArePast_thenThrowError() throws Exception {
        RecommendationRequest request =
            new RecommendationRequest(
                1,
                LocalDate.now().minusDays(2),
                LocalTime.now().minusHours(2),
                new UserPreferences(false, false, false, false)
            );
        when(recommendService.getRecommendedTable(any(RecommendationRequest.class)))
            .thenThrow(new RuntimeException("Reservation date cannot be in the past!"));

        mockMvc.perform(post("/recommended-table")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());
    }

    @Test
    void givenInvalidRequestParam_whenSaveTables_thenReturnsError() throws Exception {
        String json = """
                {
                    "partySize": 0,
                    "date": null,
                    "time": null
                }
            """;

        mockMvc.perform(post("/recommended-table")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("$.errors.date").value("Date is required!"))
            .andExpect(jsonPath("$.errors.time").value("Time is required!"))
            .andExpect(jsonPath("$.errors.partySize").value("must be greater than or equal to 1"));
    }
}