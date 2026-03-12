package ee.margus.resto_reserv_app.repository;

import ee.margus.resto_reserv_app.model.Reservation;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ReservationRepository {
    private final ConcurrentHashMap<Long, Reservation> reservations = new ConcurrentHashMap<>();
    // Gemini answer for thread-safe ID generation
    private final AtomicLong idGenerator = new AtomicLong(0);

    public List<Reservation> findAll() {
        return reservations.values().stream().toList();
    }

    public Reservation save(Reservation reservation){
        Long id = idGenerator.incrementAndGet();
        reservation.setId(id);
        reservations.put(id, reservation);
        return reservation;
    }

    public void delete(Long id){
        reservations.remove(id);
    }
}
