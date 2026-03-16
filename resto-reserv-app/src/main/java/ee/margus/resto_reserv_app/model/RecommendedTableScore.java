package ee.margus.resto_reserv_app.model;

import ee.margus.resto_reserv_app.entity.RestaurantTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecommendedTableScore {
    private RestaurantTable restaurantTable;
    private int score;
}
