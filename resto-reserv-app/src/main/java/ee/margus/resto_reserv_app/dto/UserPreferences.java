package ee.margus.resto_reserv_app.dto;

public record UserPreferences(Boolean isWindow,
                              Boolean isPrivate,
                              Boolean isEasyAccess,
                              Boolean isNearKidsArea) {
}
