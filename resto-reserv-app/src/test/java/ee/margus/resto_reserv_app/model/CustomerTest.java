package ee.margus.resto_reserv_app.model;

import ee.margus.resto_reserv_app.entity.Customer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerTest {

    @Test
    void cleanNumber() {
        String name = "Test";
        String phoneNumber = "(+372) 55 55-5555";
        Customer customer = new Customer(name, phoneNumber);
        assertEquals("+37255555555", customer.getPhoneNumber());
    }

}