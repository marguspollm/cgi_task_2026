package ee.margus.resto_reserv_app.model;

import lombok.Data;

import java.util.List;

@Data
public class MealDbResponse {
    private List<MealDbMeal> meals;
}
