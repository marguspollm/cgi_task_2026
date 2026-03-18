package ee.margus.resto_reserv_app.controller;

import ee.margus.resto_reserv_app.dto.RecommendationRequest;
import ee.margus.resto_reserv_app.service.RecommendService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for getting recommended table
 */
@RestController
public class RecommendController {
    @Autowired
    private RecommendService recommendService;

    /**
     * POST endpoint to get a table recommendation
     * Analyzes available tables and recommends the best match based on party size
     * and preferences
     * 
     * @param request The recommendation parameters (party size, date, time, table,
     *                table preferences)
     * @return ID of the recommended table
     */
    @PostMapping("recommended-table")
    public Long getRecommendedTable(@RequestBody @Valid RecommendationRequest request) {
        return recommendService.getRecommendedTable(request);
    }

}
