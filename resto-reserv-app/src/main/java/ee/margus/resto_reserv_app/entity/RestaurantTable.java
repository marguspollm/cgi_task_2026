package ee.margus.resto_reserv_app.entity;

import ee.margus.resto_reserv_app.model.TableAttribute;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RestaurantTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int capacity;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
        name = "table_attributes",
        joinColumns = @JoinColumn(name = "table_id")
    )
    private Set<TableAttribute> attribute;

    private int locationX;
    private int locationY;

    @Override
    public String toString() {
        return "Table{" +
            "id=" + id +
            ", capacity=" + capacity +
            ", attribute=" + attribute +
            ", locationX=" + locationX +
            ", locationY=" + locationY +
            '}';
    }
}
