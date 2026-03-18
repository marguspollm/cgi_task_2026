package ee.margus.resto_reserv_app.service;

import ee.margus.resto_reserv_app.dto.MealDto;
import ee.margus.resto_reserv_app.model.MealDbMealData;
import ee.margus.resto_reserv_app.model.MealDbResponse;
import ee.margus.resto_reserv_app.util.RandomGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class MealService {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${mealDb.url}")
    private String mealDbUrl;

    public List<MealDto> getRecommendedMeals() {
        List<MealDto> meals = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            MealDbResponse response =
                restTemplate.exchange(mealDbUrl, HttpMethod.GET, null, MealDbResponse.class)
                    .getBody();

            if (response != null && response.getMeals() != null && !response.getMeals().isEmpty()) {
                MealDbMealData meal = response.getMeals().getFirst();
                MealDto mealDto =
                    new MealDto(
                        Long.valueOf(meal.getIdMeal()),
                        meal.getStrMeal(),
                        meal.getStrCategory(),
                        meal.getStrMealThumb(),
                        RandomGenerator.price()
                    );
                if(!meals.contains(mealDto)) meals.add(mealDto);
            }
        }

        return meals;
    }
}
