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

    // Filter reservations (with help from Gemini)
    @Query("""
        SELECT r FROM Reservation r WHERE
                (:date IS NULL OR r.date = :date) AND
                (:time IS NULL OR r.startTime = :time) AND
                (:name IS NULL OR :name = '' OR LOWER(r.customer.name) LIKE LOWER(CONCAT('%', :name, '%')))
                ORDER BY r.date ASC, r.startTime ASC
        """)
    Page<Reservation> findWithOptionalFilters(LocalDate date, LocalTime time, String name, Pageable pageable);

    boolean existsByRestaurantTable_IdAndDateGreaterThanEqual(Long id, LocalDate date);

    List<Reservation> findByDateAndRestaurantTable_IdAndStartTimeLessThanAndEndTimeGreaterThan(
        LocalDate date,
        Long tableId,
        LocalTime endTime,
        LocalTime startTime
    );

    List<Reservation> findByDateAndStartTimeLessThanAndEndTimeGreaterThan(
        LocalDate date,
        LocalTime endTime,
        LocalTime startTime
    );
}
