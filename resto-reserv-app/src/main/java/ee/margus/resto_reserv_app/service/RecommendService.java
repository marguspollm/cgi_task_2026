package ee.margus.resto_reserv_app.service;

import ee.margus.resto_reserv_app.dto.RecommendationRequest;
import ee.margus.resto_reserv_app.model.RecommendedTableScore;
import ee.margus.resto_reserv_app.repository.TableRepository;
import ee.margus.resto_reserv_app.util.TableScoreCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ee.margus.resto_reserv_app.util.Validator.validateRequest;

@Service
public class RecommendService {
    @Autowired
    private TableRepository tableRepository;

    /**
     * Recommends the best available table based on request criteria
     * 
     * @param request The recommendation criteria (party size, date, time, preferred
     *                attributes)
     * @return ID of the recommended table
     * @throws RuntimeException if no available tables meet the requirements
     */
    @Transactional(readOnly = true)
    public Long getRecommendedTable(RecommendationRequest request) {
        validateRequest(request);

        // Get all tables that are equal or greater than party size and map to
        // RecommendedTableScore
        List<RecommendedTableScore> availableTableScore = tableRepository
                .findAvailableTables(
                        request.partySize(),
                        request.date(),
                        request.time(),
                        request.time().plusHours(2))
                .stream()
                .map(table -> new RecommendedTableScore(table, 0))
                .collect(Collectors.toList());

        // Calculate recommendation score for each table
        availableTableScore.forEach(tableScore -> tableScore
                .setScore(TableScoreCalculator.score(tableScore, request)));

        // Sort by highest score
        availableTableScore.sort(Comparator.comparingInt(RecommendedTableScore::getScore).reversed());

        // Return
        return availableTableScore
                .stream()
                .findFirst()
                .map(ts -> ts.getRestaurantTable().getId())
                .orElseThrow(() -> new RuntimeException("No available tables to recommend!"));
    }
}
