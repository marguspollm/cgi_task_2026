package ee.margus.resto_reserv_app.util;

import ee.margus.resto_reserv_app.entity.Reservation;
import ee.margus.resto_reserv_app.entity.RestaurantTable;
import ee.margus.resto_reserv_app.model.TableAttribute;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

// Populates reservations and tables with random data for demo
public class RandomGenerator {
    private static final Random random = new Random();

    public static List<Reservation> reservations(List<RestaurantTable> restaurantTables) {

        List<Reservation> reservations = new ArrayList<>();

        while (reservations.size() < 32){

            Reservation r = new Reservation();

            LocalDate date = LocalDate.now().plusDays(random.nextInt(3));

            int hour = 10 + random.nextInt(12);
            LocalTime time = LocalTime.of(hour, 0);

            r.setDate(date);
            r.setTime(time);

            RestaurantTable restaurantTable = restaurantTables.get(random.nextInt(restaurantTables.size()));
            r.setRestaurantTable(restaurantTable);

            r.setPartySize(1 + random.nextInt(restaurantTable.getCapacity()));

            var check =  reservations.stream()
                .filter(res -> res.getDate() == date &&
                    res.getTime().isAfter(time.minusHours(2)) &&
                    res.getTime().isBefore(time.plusHours(2)) &&
                    Objects.equals(res.getRestaurantTable().getId(), restaurantTable.getId()))
                .collect(Collectors.toSet());

            if(check.isEmpty()) reservations.add(r);
        }

        return reservations;
    }

    public static List<RestaurantTable> tables() {
        List<RestaurantTable> restaurantTables = new ArrayList<>();
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
                    if (i < taValesLength)
                        attrs.add(taValues[i]);
                }

                RestaurantTable rt = new RestaurantTable();
                rt.setCapacity(random.nextInt(1, 10));
                rt.setAttributes(attrs);
                rt.setLocationX(100 + col * spacingX);
                rt.setLocationY(100 + row * spacingY);

                restaurantTables.add(rt);
            }
        }

        return restaurantTables;
    }

    public static int price() {
        return random.nextInt(100);
    }
}
