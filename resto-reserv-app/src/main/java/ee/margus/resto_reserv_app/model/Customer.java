package ee.margus.resto_reserv_app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Customer {
    private String name;
    private String phoneNumber;

    public Customer(String name, String phoneNumber){
        this.name = name;
        this.phoneNumber = cleanNumber(phoneNumber);
    }

    public String cleanNumber(String phoneNumber){
        return phoneNumber.replaceAll("[\\s\\-()]", "");
    }
}
