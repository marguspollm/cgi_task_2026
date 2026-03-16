package ee.margus.resto_reserv_app.repository;

import ee.margus.resto_reserv_app.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // Help with Gemini to filter reservations
    @Query("""
        SELECT r FROM Reservation r WHERE
                (:date IS NULL OR r.date = :date) AND
                (:time IS NULL OR r.time = :time) AND
                (:name IS NULL OR :name = '' OR LOWER(r.customer.name) LIKE LOWER(CONCAT('%', :name, '%')))
        """)
    Page<Reservation> findWithOptionalFilters(LocalDate date, LocalTime time, String name, Pageable pageable);
}
