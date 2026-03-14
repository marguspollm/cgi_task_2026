package ee.margus.resto_reserv_app.util;

import ee.margus.resto_reserv_app.dto.RecommendationRequest;
import ee.margus.resto_reserv_app.dto.UserPreferences;
import ee.margus.resto_reserv_app.model.RecommendedTableScore;
import ee.margus.resto_reserv_app.model.Table;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import static ee.margus.resto_reserv_app.model.TableAttribute.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TableScoreCalculatorTest {

    @Test
    void whenCapacityIsExactMatch_thenScoreIs10Points() {
        Table table = new Table(1L, 4, Set.of(), 10, 10);
        RecommendedTableScore rts = new RecommendedTableScore(table, 0);
        RecommendationRequest request =
            new RecommendationRequest(4, LocalDate.now(), LocalTime.now(), null);

        int score = TableScoreCalculator.score(rts, request);

        assertEquals(10, score);
    }

    @Test
    void whenCapacityPlusOne_thenScoreIs6Points() {
        Table table = new Table(1L, 5, Set.of(), 10, 10);
        RecommendedTableScore rts = new RecommendedTableScore(table, 0);
        RecommendationRequest request =
            new RecommendationRequest(4, LocalDate.now(), LocalTime.now(), null);

        int score = TableScoreCalculator.score(rts, request);

        assertEquals(6, score);
    }

    @Test
    void whenCapacityPlusTwo_thenScoreIs3Points() {
        Table table = new Table(1L, 6, Set.of(), 10, 10);
        RecommendedTableScore rts = new RecommendedTableScore(table, 0);
        RecommendationRequest request =
            new RecommendationRequest(4, LocalDate.now(), LocalTime.now(), null);

        int score = TableScoreCalculator.score(rts, request);

        assertEquals(3, score);
    }

    @Test
    void whenTooManyExtraSeats_thenApplyPenaltyFor() {
        Table table = new Table(1L, 8, Set.of(), 10, 10);
        RecommendedTableScore rts = new RecommendedTableScore(table, 0);
        RecommendationRequest request =
            new RecommendationRequest(4, LocalDate.now(), LocalTime.now(), null);

        int score = TableScoreCalculator.score(rts, request);

        assertEquals(-4, score);
    }

    @Test
    void whenWindowPreference_thenAddPointsForWindowPreference() {
        Table table = new Table(1L, 4, Set.of(WINDOW), 10, 10);
        RecommendedTableScore rts = new RecommendedTableScore(table, 0);
        RecommendationRequest request =
            new RecommendationRequest(
                4,
                LocalDate.now(),
                LocalTime.now(),
                new UserPreferences(true, null, null, null));

        int score = TableScoreCalculator.score(rts, request);

        assertEquals(12, score);
    }

    @Test
    void whenMultiplePreferences_thenAddPointsForMultiplePreferences() {
        Table table = new Table(1L, 4, Set.of(PRIVATE, EASY_ACCESSIBLE, NEAR_KIDS_AREA), 10, 10);
        RecommendedTableScore rts = new RecommendedTableScore(table, 0);
        RecommendationRequest request =
            new RecommendationRequest(
                4,
                LocalDate.now(),
                LocalTime.now(),
                new UserPreferences(null, true, true, true));

        int score = TableScoreCalculator.score(rts, request);

        assertEquals(16, score);
    }

    @Test
    void whenNoPreference_thenDoesNotAddPointsPreferencePoints() {
        Table table = new Table(1L, 4, Set.of(WINDOW), 10, 10);
        RecommendedTableScore rts = new RecommendedTableScore(table, 0);
        RecommendationRequest request =
            new RecommendationRequest(
                4,
                LocalDate.now(),
                LocalTime.now(),
                new UserPreferences(false, false, false, false));

        int score = TableScoreCalculator.score(rts, request);

        assertEquals(10, score);
    }
}