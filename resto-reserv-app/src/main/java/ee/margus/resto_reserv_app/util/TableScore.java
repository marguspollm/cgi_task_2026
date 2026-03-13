package ee.margus.resto_reserv_app.util;

import ee.margus.resto_reserv_app.dto.RecommendationRequest;
import ee.margus.resto_reserv_app.dto.UserPreferences;
import ee.margus.resto_reserv_app.model.RecommendedTableScore;
import ee.margus.resto_reserv_app.model.Table;
import ee.margus.resto_reserv_app.model.TableAttribute;

import java.util.Set;

import static ee.margus.resto_reserv_app.model.TableAttribute.*;

public class TableScore {
    public static int score(RecommendedTableScore ts, RecommendationRequest request) {
        int score = 0;
        UserPreferences userPreferences = request.userPreferences();
        Table table = ts.getTable();
        Set<TableAttribute> attr = table.getAttribute();

        if (table.getCapacity() == request.partySize()) {
            score += 10;
        } else if (table.getCapacity() == request.partySize() + 1) {
            score += 6;
        } else if (table.getCapacity() == request.partySize() + 2) {
            score += 3;
        }

        int extraSeats = table.getCapacity() - request.partySize();
        if (extraSeats > 2) score -= extraSeats;

        if (attr.contains(WINDOW) && Boolean.TRUE.equals(userPreferences.isWindow()))
            score += 2;
        if (attr.contains(PRIVATE) && Boolean.TRUE.equals(userPreferences.isPrivate()))
            score += 2;
        if (attr.contains(EASY_ACCESSIBLE) && Boolean.TRUE.equals(userPreferences.isEasyAccess()))
            score += 2;
        if (attr.contains(NEAR_KIDS_AREA) && Boolean.TRUE.equals(userPreferences.isNearKidsArea()))
            score += 2;

        return score;
    }
}
