package ee.margus.resto_reserv_app.controller;

import ee.margus.resto_reserv_app.dto.MealDto;
import ee.margus.resto_reserv_app.service.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for managing meal recommendations
 * Provides endpoint to fetch recommended meals from external API
 */
@RestController
public class MealController {
    @Autowired
    private MealService mealService;

    /**
     * GET endpoint to retrieve recommended meals for the restaurant
     * Fetches meals from external MealDB API with randomly generated prices
     * 
     * @return List of recommended MealDto objects with details
     */
    @GetMapping("meals")
    public List<MealDto> getMeals() {
        return mealService.getRecommendedMeals();
    }
}
