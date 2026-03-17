package ee.margus.resto_reserv_app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private LocalTime time;

    @ManyToOne
    @JoinColumn(name = "restaurant_table_id")
    private RestaurantTable restaurantTable;
    private int partySize;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Override
    public String toString() {
        return "Reservation{" +
            "id=" + id +
            ", date=" + date +
            ", time=" + time +
            ", restaurantTableId=" + (restaurantTable != null ? restaurantTable.getId() : null) +
            ", partySize=" + partySize +
            ", customerId=" + (customer != null ? customer.getId() : null) +
            '}';
    }
}
