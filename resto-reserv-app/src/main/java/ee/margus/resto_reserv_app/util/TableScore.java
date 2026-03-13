package ee.margus.resto_reserv_app.util;

import ee.margus.resto_reserv_app.dto.RecommendationRequest;
import ee.margus.resto_reserv_app.dto.UserPreferences;
import ee.margus.resto_reserv_app.model.RecommendedTableScore;
import ee.margus.resto_reserv_app.model.Table;
import ee.margus.resto_reserv_app.model.TableAttribute;

import static ee.margus.resto_reserv_app.model.TableAttribute.*;

public class TableScore {
    public static int score(RecommendedTableScore ts, RecommendationRequest request) {
        int score = 0;
        UserPreferences userPreferences = request.userPreferences();
        Table table = ts.getTable();
        TableAttribute attr = table.getAttribute();

        if (table.getCapacity() == request.partySize()) score += 3;
        else if(table.getCapacity() == request.partySize() + 1 || table.getCapacity() == request.partySize() + 2) score += 1;


        if (attr == WINDOW && Boolean.TRUE.equals(userPreferences.isWindow()))
            score++;

        if (attr == PRIVATE && Boolean.TRUE.equals(userPreferences.isPrivate()))
            score++;

        if (attr == EASY_ACCESSIBLE && Boolean.TRUE.equals(userPreferences.isEasyAccess()))
            score++;

        if (attr == NEAR_KIDS_AREA && Boolean.TRUE.equals(userPreferences.isNearKidsArea()))
            score++;

        return score;
    }
}
