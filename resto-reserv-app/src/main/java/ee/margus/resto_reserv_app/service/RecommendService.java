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

    @Transactional(readOnly = true)
    public Long getRecommendedTable(RecommendationRequest request) {
        validateRequest(request);

        List<RecommendedTableScore> availableTableScore = tableRepository
            .findByCapacityGreaterThanEqual(
                request.partySize(),
                request.date(),
                request.time().minusHours(2),
                request.time().plusHours(2)
            )
            .stream()
            .map(table -> new RecommendedTableScore(table, 0))
            .collect(Collectors.toList());

        availableTableScore.forEach(tableScore -> tableScore
            .setScore(TableScoreCalculator.score(tableScore, request)));

        availableTableScore.sort(Comparator.comparingInt(RecommendedTableScore::getScore).reversed());

        return availableTableScore
            .stream()
            .findFirst()
            .map(ts -> ts.getRestaurantTable().getId())
            .orElseThrow(() -> new RuntimeException("No available tables to recommend!"));
    }
}
