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

/**
 * REST controller for managing restaurant reservations
 * Provides endpoints for creating reservations, retrieving reserved tables, and
 * filtering reservations
 */
@RestController
public class ReservationController {
    @Autowired
    private ReservationService service;

    /**
     * POST endpoint to create a new reservation
     * Validates the reservation request and saves it to the database
     * 
     * @param reservationRequest Reservation details (must be valid)
     * @return Created reservation with confirmation details
     */
    @PostMapping("create-reservation")
    public ReservationResponse createReservation(@Valid @RequestBody ReservationRequest reservationRequest) {
        return service.create(reservationRequest);
    }

    /**
     * GET endpoint to retrieve reserved tables for a specific date and time
     * If date or time are not provided, defaults to current date and time
     * 
     * @param date Reservation date (optional, defaults to today)
     * @param time Reservation time (optional, defaults to now)
     * @return List of table IDs that are currently reserved
     */
    @GetMapping("reserved-tables")
    public List<Long> getReservedTables(@RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) LocalTime time) {
        // Use current date and/or time if not provided
        if (date == null)
            date = LocalDate.now();
        if (time == null)
            time = LocalTime.now();

        return service.getReservedTables(date, time);
    }

    /**
     * GET endpoint to retrieve filtered reservations
     * Allows filtering by date, time, and customer name
     * 
     * @param reservationFilters Optional filters (date, time, customerName)
     * @param pageable           Pagination parameters (page, size, sort)
     * @return Paginated response of reservations matching the filters
     */
    @GetMapping("reservations")
    public Page<ReservationResponse> getFilteredReservations(
            ReservationFilters reservationFilters,
            Pageable pageable) {
        return service.getFilteredReservations(reservationFilters, pageable);
    }

}
