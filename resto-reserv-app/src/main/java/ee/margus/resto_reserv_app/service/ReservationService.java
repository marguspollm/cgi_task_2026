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

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private TableRepository tableRepository;

    public ReservationResponse create(ReservationRequest reservationRequest) {
        validateRequest(reservationRequest);

        Customer customer = getCustomer(reservationRequest);
        Reservation reservation = createReservation(reservationRequest);

        reservation.setCustomer(customer);

        Reservation savedReservation = reservationRepository.save(reservation);

        return createReservationResponse(savedReservation);
    }

    private static @NonNull Customer getCustomer(ReservationRequest reservationRequest) {
        String cleanNumber = reservationRequest.phoneNumber().replaceAll("[\\s\\-()]", "");
        return new Customer(reservationRequest.customerName(), cleanNumber);
    }

    private void validateRequest(ReservationRequest request) {
        if (request.date().isBefore(LocalDate.now())) {
            throw new RuntimeException("Reservation date cannot be in the past!");
        }
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

    private @NonNull Reservation createReservation(ReservationRequest reservationRequest) {
        Reservation reservation = new Reservation();

        reservation.setDate(reservationRequest.date());
        reservation.setTime(reservationRequest.time());
        reservation.setPartySize(reservationRequest.partySize());

        Table dbTable = tableRepository.findById(reservationRequest.tableId());
        reservation.setTable(dbTable);
        return reservation;
    }
}
