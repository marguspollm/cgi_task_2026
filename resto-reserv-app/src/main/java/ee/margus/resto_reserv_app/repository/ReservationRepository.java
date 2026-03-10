package ee.margus.resto_reserv_app.repository;

import ee.margus.resto_reserv_app.model.Reservation;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ReservationRepository {
    private final ConcurrentHashMap<Long, Reservation> reservations = new ConcurrentHashMap<>();
}
