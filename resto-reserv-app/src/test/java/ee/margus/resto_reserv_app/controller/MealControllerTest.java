package ee.margus.resto_reserv_app.controller;

import ee.margus.resto_reserv_app.dto.MealDto;
import ee.margus.resto_reserv_app.model.MealDbResponse;
import ee.margus.resto_reserv_app.service.MealService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MealController.class)
class MealControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MealService mealService;

    @Test
    void getMeals_thenReturnList() throws Exception {
        MealDto meal = new MealDto(1L, "Chicken soup", "Soup", "image.png", 10);
        List<MealDto> mockMeals = List.of(meal);

        when(mealService.getRecommendedMeals()).thenReturn(mockMeals);

        mockMvc.perform(get("/meals")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(1))
            .andExpect(jsonPath("$[0].name").value("Chicken soup"))
            .andExpect(jsonPath("$[0].price").value(10));
    }

    @Test
    void getMeals_whenServiceFails_thenReturnInternalServerError() throws Exception {
        when(mealService.getRecommendedMeals()).thenThrow(new RuntimeException("Something went wrong"));

        mockMvc.perform(get("/meals"))
            .andExpect(status().isInternalServerError());
    }

}