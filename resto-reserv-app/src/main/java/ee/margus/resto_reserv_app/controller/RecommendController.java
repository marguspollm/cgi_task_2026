package ee.margus.resto_reserv_app.controller;

import ee.margus.resto_reserv_app.dto.RecommendationRequest;
import ee.margus.resto_reserv_app.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecommendController {
    @Autowired
    private RecommendService recommendService;

    @PostMapping("recommended-table")
    public Long getRecommendedTable(@RequestBody RecommendationRequest request) {
        return recommendService.getRecommendedTable(request).orElseThrow();
    }

}
