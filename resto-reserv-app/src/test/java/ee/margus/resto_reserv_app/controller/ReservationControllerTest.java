package ee.margus.resto_reserv_app.controller;

import ee.margus.resto_reserv_app.dto.ReservationRequest;
import ee.margus.resto_reserv_app.dto.ReservationResponse;
import ee.margus.resto_reserv_app.service.ReservationService;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
        ReservationRequest request = getReservationRequest();
        ReservationResponse response = getReservationResponse();

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

        mockMvc.perform(get("/reserved-tables")
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

        mockMvc.perform(get("/reserved-tables"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0]").value(1L));
    }

    @Test
    void givenInvalidRequestParams_whenCreateReservation_thenReturnErrors() throws Exception {
        String json = """
                {
                    "customerName": "",
                    "phoneNumber": "1",
                    "tableId": null,
                    "date": "",
                    "time": "",
                    "partySize": 0
                }
            """;

        mockMvc.perform(post("/create-reservation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("$.errors.date").value("Date is required!"))
            .andExpect(jsonPath("$.errors.time").value("Time is required!"))
            .andExpect(jsonPath("$.errors.partySize").value("must be greater than or equal to 1"))
            .andExpect(jsonPath("$.errors.customerName").value("Customer name is required!"))
            .andExpect(jsonPath("$.errors.phoneNumber").value("Invalid phone number!"));
    }

    @Test
    void givenFilters_whenGetFilteredReservations_thenReturnsPage() throws Exception {
        ReservationResponse response = getReservationResponse();

            Page < ReservationResponse > page =
                new PageImpl<>(List.of(response), PageRequest.of(0, 10), 1);

        when(reservationService.getFilteredReservations(any(), any())).thenReturn(page);

        mockMvc.perform(get("/reservations")
                .param("page", "0")
                .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()").value(1))
            .andExpect(jsonPath("$.content[0].id").value(1))
            .andExpect(jsonPath("$.page.totalElements").value(1))
            .andExpect(jsonPath("$.page.size").value(10));
    }


    private static @NonNull ReservationResponse getReservationResponse() {
        return new ReservationResponse(
            1L,
            "Test Tester",
            "12345678",
            1L,
            LocalDate.now(),
            LocalTime.now(),
            2
        );
    }

    private static @NonNull ReservationRequest getReservationRequest() {
        return new ReservationRequest(
            "Test Tester",
            "12345678",
            1L,
            LocalDate.now(),
            LocalTime.now(),
            2
        );
    }
}