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
    // Get all tables that are booked and match customer party size for
    // Recommendation service (with help from Gemini AI)
    @Query("""
        SELECT rt
            FROM RestaurantTable rt
            WHERE rt.capacity >= :capacity
            AND rt.id NOT IN (
                SELECT r.restaurantTable.id
                FROM Reservation r
                WHERE r.date = :date
                AND r.time BETWEEN :startTime AND :endTime
            )
            ORDER BY rt.capacity ASC
        """)
    List<RestaurantTable> findAvailableTablesByCapacityAndReservation_DateAndReservation_TimeBetween(int capacity, LocalDate date, LocalTime startTime,
                                                                                                     LocalTime endTime);
}
