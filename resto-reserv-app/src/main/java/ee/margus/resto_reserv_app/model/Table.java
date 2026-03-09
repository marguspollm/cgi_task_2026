package ee.margus.resto_reserv_app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Table {
    private Long id;
    private int capacity;
    private TableAttribute attribute;

    private Integer locationX;
    private Integer locationY;
}
