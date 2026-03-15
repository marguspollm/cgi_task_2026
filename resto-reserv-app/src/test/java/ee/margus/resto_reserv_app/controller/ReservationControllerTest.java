package ee.margus.resto_reserv_app.controller;

import ee.margus.resto_reserv_app.dto.ReservationRequest;
import ee.margus.resto_reserv_app.dto.ReservationResponse;
import ee.margus.resto_reserv_app.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationService reservationService;

    @Test
    void createReservation_thenReturnResponse() throws Exception {
        ReservationRequest request =
            new ReservationRequest(
                "Test Tester",
                "12345678",
                1L,
                LocalDate.now(),
                LocalTime.now(),
                2
            );
        ReservationResponse response =
            new ReservationResponse(
                1L,
                "Test Tester",
                "12345678",
                1L,
                LocalDate.now(),
                LocalTime.now(),
                2
            );

        when(reservationService.create(any())).thenReturn(response);

        mockMvc.perform(post("/create-reservation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void getReservedTablesWithParams_thenReturnTables() throws Exception {
        List<Long> tables = List.of(1L, 2L, 3L);

        when(reservationService.getReservedTables(
            LocalDate.of(2026, 3, 15),
            LocalTime.of(18, 0)))
            .thenReturn(tables);

        mockMvc.perform(get("/reservations")
                .param("date", "2026-03-15")
                .param("time", "18:00"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0]").value(1L))
            .andExpect(jsonPath("$[1]").value(2L))
            .andExpect(jsonPath("$[2]").value(3L));
    }

    @Test
    void getReservedTablesWithoutParams_thenUseDefaults() throws Exception {
        when(reservationService.getReservedTables(any(), any())).thenReturn(List.of(1L));

        mockMvc.perform(get("/reservations"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0]").value(1L));
    }
}