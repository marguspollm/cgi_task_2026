package ee.margus.resto_reserv_app.service;

import ee.margus.resto_reserv_app.dto.RecommendationRequest;
import ee.margus.resto_reserv_app.model.RecommendedTableScore;
import ee.margus.resto_reserv_app.model.Reservation;
import ee.margus.resto_reserv_app.model.Table;
import ee.margus.resto_reserv_app.repository.ReservationRepository;
import ee.margus.resto_reserv_app.repository.TableRepository;
import ee.margus.resto_reserv_app.util.TableScore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ee.margus.resto_reserv_app.util.Validator.validateRequest;

@Service
public class RecommendService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private TableRepository tableRepository;

    public Optional<Long> getRecommendedTable(RecommendationRequest request) {
        validateRequest(request);

        List<Table> notAvailableTables = reservationRepository.findAll().stream()
            .filter(reservation -> reservation.getDate().equals(request.date()))
            .filter(reservation ->
                reservation.getTime().isBefore(request.time().plusHours(2))
                    && reservation.getTime().plusHours(2).isAfter(request.time())
            )
            .map(Reservation::getTable)
            .toList();

        List<RecommendedTableScore> availableTableScore = tableRepository.findAll()
            .stream()
            .filter(table -> !notAvailableTables.contains(table))
            .filter(table -> table.getCapacity() >= request.partySize())
            .map(table -> new RecommendedTableScore(table, 0))
            .collect(Collectors.toList());

        availableTableScore.forEach(tableScore -> tableScore
            .setScore(TableScore.score(tableScore, request)));

        availableTableScore.sort(Comparator.comparingInt(RecommendedTableScore::getScore).reversed());

        return Optional.of(availableTableScore.getFirst().getTable().getId());
    }
}
