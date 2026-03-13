package ee.margus.resto_reserv_app.util;

import ee.margus.resto_reserv_app.model.Customer;
import ee.margus.resto_reserv_app.model.Reservation;
import ee.margus.resto_reserv_app.model.Table;
import ee.margus.resto_reserv_app.model.TableAttribute;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

// Creates random reservations and tables for demo
public class RandomGenerator {
    private static final Random random = new Random();

    public static List<Reservation> generateReservations(List<Table> tables) {

        List<Reservation> reservations = new ArrayList<>();

        for (int i = 0; i < 31; i++) {

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

            r.setPartySize(1 + random.nextInt(table.getCapacity()));

            reservations.add(r);
        }

        return reservations;
    }

    public static List<Table> generateTables() {
        List<Table> tables = new ArrayList<>();
        long id = 1;
        int spacingX = 100;
        int spacingY = 100;
        TableAttribute[] taValues = TableAttribute.values();

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {

                int attrCount = random.nextInt(3);
                Set<TableAttribute> attrs = new HashSet<>();
                for (int j = 0; j < attrCount; j++) {
                    int taValesLength = taValues.length;
                    int i = random.nextInt(taValesLength);
                    if (i < taValesLength) attrs.add(taValues[i]);
                }


                Table table = new Table(
                    id++,
                    random.nextInt(1, 10),
                    attrs,
                    100 + col * spacingX,
                    100 + row * spacingY
                );
System.out.println(table);
                tables.add(table);
            }
        }

        return tables;
    }
}
