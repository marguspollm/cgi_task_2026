package ee.margus.resto_reserv_app.controller;

import ee.margus.resto_reserv_app.dto.MealDto;
import ee.margus.resto_reserv_app.service.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MealController {
    @Autowired
    private MealService mealService;

    @GetMapping("meals")
    public List<MealDto> getMeals() {
        return mealService.getRecommendedMeals();
    }
}
