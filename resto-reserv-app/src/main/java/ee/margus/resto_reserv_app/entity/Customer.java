package ee.margus.resto_reserv_app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phoneNumber;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations = new ArrayList<>();

    public Customer(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = cleanNumber(phoneNumber);
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = cleanNumber(phoneNumber);
    }

    private String cleanNumber(String phoneNumber) {
        return phoneNumber.replaceAll("[\\s\\-()]", "");
    }
}
