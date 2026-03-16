package ee.margus.resto_reserv_app.repository;

import ee.margus.resto_reserv_app.entity.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface TableRepository extends JpaRepository<RestaurantTable, Long> {
    // Help from Gemini
    @Query("""
        SELECT rt
        FROM RestaurantTable rt
        LEFT JOIN Reservation r
            ON r.restaurantTable = rt
            AND r.date = :date
            AND r.time BETWEEN :startTime AND :endTime
        WHERE rt.capacity >= :capacity
        AND r.id IS NULL
        ORDER BY rt.capacity ASC
        """)
    List<RestaurantTable> findByCapacityGreaterThanEqual(int capacity, LocalDate date, LocalTime startTime, LocalTime endTime);
}
