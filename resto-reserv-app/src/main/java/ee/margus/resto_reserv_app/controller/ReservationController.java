package ee.margus.resto_reserv_app.controller;

import ee.margus.resto_reserv_app.dto.ReservationFilters;
import ee.margus.resto_reserv_app.dto.ReservationRequest;
import ee.margus.resto_reserv_app.dto.ReservationResponse;
import ee.margus.resto_reserv_app.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
public class ReservationController {
    @Autowired
    private ReservationService service;

    @PostMapping("create-reservation")
    public ReservationResponse createReservation(@Valid @RequestBody ReservationRequest reservationRequest) {
        return service.create(reservationRequest);
    }

    @GetMapping("reserved-tables")
    public List<Long> getReservedTables(@RequestParam(required = false) LocalDate date,
                                        @RequestParam(required = false) LocalTime time) {
        if (date == null) date = LocalDate.now();
        if (time == null) time = LocalTime.now();

        return service.getReservedTables(date, time);
    }

    @GetMapping("reservations")
    public Page<ReservationResponse> getAllReservations(
        ReservationFilters reservationFilters,
        Pageable pageable) {
        return service.getAllReservations(reservationFilters, pageable);
    }

}
