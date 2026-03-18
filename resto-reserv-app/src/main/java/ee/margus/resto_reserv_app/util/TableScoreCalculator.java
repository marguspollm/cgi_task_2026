package ee.margus.resto_reserv_app.util;

import ee.margus.resto_reserv_app.dto.RecommendationRequest;
import ee.margus.resto_reserv_app.entity.RestaurantTable;
import ee.margus.resto_reserv_app.model.RecommendedTableScore;
import ee.margus.resto_reserv_app.model.TableAttribute;

import java.util.Set;

import static ee.margus.resto_reserv_app.model.TableAttribute.*;

public class TableScoreCalculator {
    public static int score(RecommendedTableScore ts, RecommendationRequest request) {
        int score = 0;
        Set<TableAttribute> tablePreferences = request.tablePreferences();
        RestaurantTable restaurantTable = ts.getRestaurantTable();
        Set<TableAttribute> attr = restaurantTable.getAttributes();

        if (restaurantTable.getCapacity() == request.partySize()) {
            score += 10;
        } else if (restaurantTable.getCapacity() == request.partySize() + 1) {
            score += 6;
        } else if (restaurantTable.getCapacity() == request.partySize() + 2) {
            score += 3;
        }

        int extraSeats = restaurantTable.getCapacity() - request.partySize();
        if (extraSeats > 2) score -= extraSeats;

        if (attr.contains(WINDOW) && tablePreferences.contains(WINDOW))
            score += 2;
        if (attr.contains(PRIVATE) && tablePreferences.contains(PRIVATE))
            score += 2;
        if (attr.contains(EASY_ACCESSIBLE) && tablePreferences.contains(EASY_ACCESSIBLE))
            score += 2;
        if (attr.contains(NEAR_KIDS_AREA) && tablePreferences.contains(NEAR_KIDS_AREA))
            score += 2;

        return score;
    }
}
