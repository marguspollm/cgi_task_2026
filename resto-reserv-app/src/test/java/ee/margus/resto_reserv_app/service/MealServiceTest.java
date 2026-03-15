package ee.margus.resto_reserv_app.service;

import ee.margus.resto_reserv_app.dto.MealDto;
import ee.margus.resto_reserv_app.model.MealDbMeal;
import ee.margus.resto_reserv_app.model.MealDbResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MealServiceTest {
    private final String testUrl = "test.com";
    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private MealService mealService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(mealService, "mealDbUrl", testUrl);
    }

    @Test
    void getRecommendedMeals_shouldReturnFourMeals() {
        MealDbMeal meal = new MealDbMeal();
        meal.setIdMeal("123");
        meal.setStrMeal("Test Meal");

        MealDbResponse mockResponse = new MealDbResponse();
        mockResponse.setMeals(List.of(meal));

        when(restTemplate.exchange(
            testUrl,
            HttpMethod.GET,
            null,
            MealDbResponse.class))
            .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        List<MealDto> result = mealService.getRecommendedMeals();

        assertEquals(4, result.size());
        assertEquals("Test Meal", result.getFirst().name());
    }

    @Test
    void getRecommendedMeals_whenApiReturnsEmpty_thenReturnEmptyList() {
        MealDbResponse emptyResponse = new MealDbResponse();
        when(restTemplate.exchange(
            testUrl,
            HttpMethod.GET,
            null,
            MealDbResponse.class))
            .thenReturn(new ResponseEntity<>(emptyResponse, HttpStatus.OK));

        List<MealDto> result = mealService.getRecommendedMeals();

        assertTrue(result.isEmpty());
    }

    @Test
    void getRecommendedMeals_whenApiFails_throwsException() {
        when(restTemplate.exchange(
                testUrl,
                HttpMethod.GET,
                null,
                MealDbResponse.class))
            .thenThrow(new RestClientException("API is down"));

        assertThrows(RestClientException.class, () -> {
            mealService.getRecommendedMeals();
        });
    }

}