package ee.margus.resto_reserv_app.service;

import ee.margus.resto_reserv_app.dto.ReservationRequest;
import ee.margus.resto_reserv_app.dto.ReservationResponse;
import ee.margus.resto_reserv_app.model.Customer;
import ee.margus.resto_reserv_app.model.Reservation;
import ee.margus.resto_reserv_app.model.Table;
import ee.margus.resto_reserv_app.repository.ReservationRepository;
import ee.margus.resto_reserv_app.repository.TableRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private TableRepository tableRepository;

    private static @NonNull Customer getCustomer(ReservationRequest reservationRequest) {
        String cleanNumber = reservationRequest.phoneNumber().replaceAll("[\\s\\-()]", "");
        return new Customer(reservationRequest.customerName(), cleanNumber);
    }

    private static @NonNull ReservationResponse createReservationResponse(Reservation savedReservation) {
        return new ReservationResponse(
            savedReservation.getId(),
            savedReservation.getCustomer().getName(),
            savedReservation.getCustomer().getPhoneNumber(),
            savedReservation.getTable().getId(),
            savedReservation.getDate(),
            savedReservation.getTime(),
            savedReservation.getPartySize()
        );
    }

    public ReservationResponse create(ReservationRequest reservationRequest) {
        validateRequest(reservationRequest);

        checkAvailability(reservationRequest.date(), reservationRequest.time(), reservationRequest.tableId());

        Customer customer = getCustomer(reservationRequest);
        Reservation reservation = createReservation(reservationRequest);

        reservation.setCustomer(customer);

        Reservation savedReservation = reservationRepository.save(reservation);

        return createReservationResponse(savedReservation);
    }

    private void checkAvailability(LocalDate date,
                                   LocalTime time,
                                   Long tableId) {
        List<Reservation> conflict = reservationRepository.findAll()
            .stream()
            .filter(res -> res.getTime().equals(time)
                && res.getDate().equals(date)
                && res.getTable().getId().equals(tableId))
            .toList();

        if(!conflict.isEmpty()) throw new RuntimeException("Table is already booked for this time");
    }

    private void validateRequest(ReservationRequest request) {
        if (request.date().isBefore(LocalDate.now())) {
            throw new RuntimeException("Reservation date cannot be in the past!");
        }
    }

    private @NonNull Reservation createReservation(ReservationRequest reservationRequest) {
        Reservation reservation = new Reservation();

        reservation.setDate(reservationRequest.date());
        reservation.setTime(reservationRequest.time());
        reservation.setPartySize(reservationRequest.partySize());

        Table dbTable = tableRepository.findById(reservationRequest.tableId());
        reservation.setTable(dbTable);
        return reservation;
    }

    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAll()
            .stream()
            .map(res -> new ReservationResponse(
                res.getId(),
                res.getCustomer().getName(),
                res.getCustomer().getPhoneNumber(),
                res.getTable().getId(),
                res.getDate(),
                res.getTime(),
                res.getPartySize()
            )).toList();
    }
}
