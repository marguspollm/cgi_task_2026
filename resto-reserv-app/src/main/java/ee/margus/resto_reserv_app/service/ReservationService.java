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
import java.util.stream.Stream;

import static ee.margus.resto_reserv_app.util.Validator.validateRequest;

/*
 * One Reservation reserves the table for 2 hours
 */
@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private TableRepository tableRepository;

    @Transactional
    public ReservationResponse create(ReservationRequest reservationRequest) {
        validateRequest(reservationRequest);

        checkAvailability(reservationRequest.date(), reservationRequest.time(), reservationRequest.tableId());

        Customer customer = createCustomer(reservationRequest);
        Reservation reservation = createReservation(reservationRequest);

        reservation.setCustomer(customer);

        Reservation savedReservation = reservationRepository.save(reservation);

        return createReservationResponse(savedReservation);
    }

    public List<Long> getReservedTables(LocalDate date, LocalTime time) {
        return getCurrentReservations(date, time)
            .map(res -> res.getRestaurantTable().getId())
            .toList();
    }

    private void checkAvailability(LocalDate date,
                                   LocalTime time,
                                   Long tableId) {
        List<Reservation> conflicts = getCurrentReservations(date, time)
            .filter(reservation -> reservation.getRestaurantTable().getId().equals(tableId))
            .toList();

        if (!conflicts.isEmpty()) throw new RuntimeException("Table is already booked for this time");
    }

    private @NonNull Stream<Reservation> getCurrentReservations(LocalDate date, LocalTime time) {
        return reservationRepository.findAll()
            .stream()
            .filter(reservation -> reservation.getDate().equals(date))
            .filter(reservation ->
                reservation.getTime().isBefore(time.plusHours(2))
                    && reservation.getTime().plusHours(2).isAfter(time)
            );
    }

    private @NonNull Reservation createReservation(ReservationRequest reservationRequest) {
        Reservation reservation = new Reservation();

        reservation.setDate(reservationRequest.date());
        reservation.setTime(reservationRequest.time());
        reservation.setPartySize(reservationRequest.partySize());

        RestaurantTable dbRestaurantTable = tableRepository.findById(reservationRequest.tableId())
            .orElseThrow(() -> new RuntimeException("Table doesn't exist!"));
        reservation.setRestaurantTable(dbRestaurantTable);

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
            reservation.getTime(),
            reservation.getPartySize()
        );
    }

    public Page<ReservationResponse> getAllReservations(ReservationFilters filters, Pageable pageable) {
        Page<Reservation> reservations = reservationRepository
            .findWithOptionalFilters(filters.date(),filters.time(), filters.customerName(), pageable);

        return reservations.map(this::createReservationResponse);
    }
}
