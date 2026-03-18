package ee.margus.resto_reserv_app.config;

import ee.margus.resto_reserv_app.entity.Customer;
import ee.margus.resto_reserv_app.entity.Reservation;
import ee.margus.resto_reserv_app.entity.RestaurantTable;
import ee.margus.resto_reserv_app.repository.CustomerRepository;
import ee.margus.resto_reserv_app.repository.ReservationRepository;
import ee.margus.resto_reserv_app.repository.TableRepository;
import ee.margus.resto_reserv_app.util.RandomGenerator;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Populate repository with random data for a demo - help from Gemini AI
@Component
public class DemoDataLoader implements CommandLineRunner {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private TableRepository tableRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    @Transactional
    public void run(String @NonNull... args) {
        Customer customer = new Customer();
        customer.setName("Test Tester");
        customer.setPhoneNumber("555 55555");

        customerRepository.save(customer);

        List<RestaurantTable> restaurantTables = RandomGenerator.tables();

        tableRepository.saveAll(restaurantTables);
        System.out.println("Generated " + restaurantTables.size() + " random tables");

        List<Reservation> reservations = RandomGenerator.reservations(restaurantTables);
        for (Reservation res : reservations) {
            res.setCustomer(customer);
        }

        reservationRepository.saveAll(reservations);
        System.out.println("Generated " + reservations.size() + " random reservations");
    }
}
