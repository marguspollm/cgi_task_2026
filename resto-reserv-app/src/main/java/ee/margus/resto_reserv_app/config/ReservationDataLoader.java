package ee.margus.resto_reserv_app.config;

import ee.margus.resto_reserv_app.model.Reservation;
import ee.margus.resto_reserv_app.model.Table;
import ee.margus.resto_reserv_app.repository.ReservationRepository;
import ee.margus.resto_reserv_app.repository.TableRepository;
import ee.margus.resto_reserv_app.util.RandomReservationGenerator;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

// Gemini answer on how to populate repository with random data for a demo
@Component
public class ReservationDataLoader implements CommandLineRunner {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private TableRepository tableRepository;

    @Override
    public void run(String @NonNull ... args) {

        List<Table> tables = tableRepository.findAll();

        List<Reservation> reservations =
            RandomReservationGenerator.generateReservations(tables);

        reservations.forEach(reservationRepository::save);

        System.out.println("Generated " + reservations.size() + " random reservations");
    }
}
