package ee.margus.resto_reserv_app.util;

import ee.margus.resto_reserv_app.dto.RecommendationRequest;
import ee.margus.resto_reserv_app.model.RecommendedTableScore;

import static ee.margus.resto_reserv_app.model.TableAttribute.*;

public class TableScore {
    public static int score(RecommendedTableScore ts, RecommendationRequest request){
        int score = 0;

        if (ts.getTable().getCapacity() == request.partySize())
            score += 2;
        if (request.userPreferences().isWindow() != null && ts.getTable().getAttribute().equals(WINDOW))
            score ++;
        if (request.userPreferences().isPrivate() != null && ts.getTable().getAttribute().equals(PRIVATE))
            score++;
        if (request.userPreferences().isEasyAccess() != null && ts.getTable().getAttribute().equals(EASY_ACCESSIBLE))
            score++;
        if (request.userPreferences().isNearKidsArea() != null && ts.getTable().getAttribute().equals(NEAR_KIDS_AREA))
            score++;

        return score;
    }
}
