package ee.margus.resto_reserv_app.service;

import ee.margus.resto_reserv_app.dto.ReservationFilters;
import ee.margus.resto_reserv_app.dto.ReservationRequest;
import ee.margus.resto_reserv_app.dto.ReservationResponse;
import ee.margus.resto_reserv_app.entity.Customer;
import ee.margus.resto_reserv_app.entity.Reservation;
import ee.margus.resto_reserv_app.entity.RestaurantTable;
import ee.margus.resto_reserv_app.repository.ReservationRepository;
import ee.margus.resto_reserv_app.repository.TableRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ee.margus.resto_reserv_app.util.Validator.validateRequest;

/*
 * Service to create and recieve all reservations
 * IMPORTANT-
 *      Reservation reserves the table for 2 hours
 *      e.g. a table booked for 15:00 can be booked again at 17:00
 *
 */
@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private TableRepository tableRepository;

    /**
     * Creates a new reservation
     *
     * @param reservationRequest The reservation details including date, time, party
     *                           size, customer info, and table ID
     * @return ReservationResponse containing the created reservation information
     * @throws RuntimeException if the table is already booked or doesn't exist
     */
    @Transactional
    public ReservationResponse create(ReservationRequest reservationRequest) {
        // Check if request date/time are not in the past
        validateRequest(reservationRequest);

        // Check if requested table is still available
        checkAvailability(reservationRequest.date(), reservationRequest.time(), reservationRequest.tableId());

        Customer customer = createCustomer(reservationRequest);
        Reservation reservation = createReservation(reservationRequest);

        reservation.setCustomer(customer);

        Reservation savedReservation = reservationRepository.save(reservation);

        return createReservationResponse(savedReservation);
    }

    /**
     * Retrieves IDs of all tables that are reserved for a specific date and time
     *
     * @param date Reservation date
     * @param time Reservation time
     * @return List of table IDs that are currently booked for the given date and
     * time
     */
    public List<Long> getReservedTables(LocalDate date, LocalTime time) {
        return reservationRepository.findByDateAndStartTimeLessThanAndEndTimeGreaterThan(date, time.plusHours(2), time)
            .stream()
            .map(res -> res.getRestaurantTable().getId())
            .toList();
    }

    /**
     * Retrieves filtered reservations from the database
     * Filters can include date, time, and customer name, if not specified, all
     * reservations are returned
     *
     * @param filters  Filters - date, time, customerName (all optional)
     * @param pageable Pagination information (page number, size, sorting)
     * @return Paginated response containing matching reservations
     */
    public Page<ReservationResponse> getFilteredReservations(ReservationFilters filters, Pageable pageable) {
        Page<Reservation> reservations = reservationRepository
            .findWithOptionalFilters(filters.date(), filters.time(), filters.customerName(), pageable);

        return reservations.map(this::createReservationResponse);
    }

    /**
     * Verifies if a table is available for reservation at the given date and time.
     * Checks for conflicts within a 2-hour window (1 hour before and after the
     * requested time)
     *
     * @param date    The desired reservation date
     * @param time    The desired reservation time
     * @param tableId ID of the table to check
     * @throws RuntimeException if the table is already booked in the specified time
     *                          window
     */
    private void checkAvailability(LocalDate date,
                                   LocalTime time,
                                   Long tableId) {
        // Check table for conflicts within 2-hour buffer
        boolean hasConflicts = reservationRepository
            .existsByDateAndRestaurantTable_IdAndStartTimeLessThanAndEndTimeGreaterThan(
                date,
                tableId,
                time.plusHours(2),
                time
            );

        if (hasConflicts)
            throw new RuntimeException("Table is already booked for this time");
    }

    private @NonNull Reservation createReservation(ReservationRequest reservationRequest) {
        Reservation reservation = new Reservation();

        // Check if given table exists in database
        RestaurantTable dbRestaurantTable = tableRepository.findById(reservationRequest.tableId())
            .orElseThrow(() -> new RuntimeException("Table doesn't exist!"));
        reservation.setRestaurantTable(dbRestaurantTable);

        reservation.setDate(reservationRequest.date());
        reservation.setStartTime(reservationRequest.time());
        // Reserve the table for 2 hours
        reservation.setEndTime(reservationRequest.time().plusHours(2));
        reservation.setPartySize(reservationRequest.partySize());

        return reservation;
    }

    private @NonNull Customer createCustomer(ReservationRequest reservationRequest) {
        return new Customer(reservationRequest.customerName(), reservationRequest.phoneNumber());
    }

    private @NonNull ReservationResponse createReservationResponse(Reservation reservation) {
        return new ReservationResponse(
            reservation.getId(),
            reservation.getCustomer().getName(),
            reservation.getCustomer().getPhoneNumber(),
            reservation.getRestaurantTable().getId(),
            reservation.getDate(),
            reservation.getStartTime(),
            reservation.getPartySize());
    }
}
