package ee.margus.resto_reserv_app.dto;

import ee.margus.resto_reserv_app.model.TableAttribute;

import java.util.Set;

public record TableDto(
    Long id,
    int capacity,
    Set<TableAttribute> attributes,
    int locationX,
    int locationY) {
}
