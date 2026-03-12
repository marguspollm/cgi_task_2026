package ee.margus.resto_reserv_app.util;

import ee.margus.resto_reserv_app.model.Customer;
import ee.margus.resto_reserv_app.model.Reservation;
import ee.margus.resto_reserv_app.model.Table;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Creates random reservation times for demo
public class RandomReservationGenerator {
    private static final Random random = new Random();

    public static List<Reservation> generateReservations(List<Table> tables) {

        List<Reservation> reservations = new ArrayList<>();

        for (int i = 0; i < 10; i++) {

            Reservation r = new Reservation();

            LocalDate date = LocalDate.now().plusDays(random.nextInt(3));

            int hour = 10 + random.nextInt(12);
            LocalTime time = LocalTime.of(hour, 0);

            r.setDate(date);
            r.setTime(time);

            Table table = tables.get(random.nextInt(tables.size()));
            r.setTable(table);

            Customer customer = new Customer("Test Tester", "55555555");
            r.setCustomer(customer);

            r.setPartySize(random.nextInt(table.getCapacity()));

            reservations.add(r);
        }

        return reservations;
    }
}
