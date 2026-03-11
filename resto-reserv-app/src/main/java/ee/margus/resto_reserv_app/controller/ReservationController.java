package ee.margus.resto_reserv_app.controller;

import ee.margus.resto_reserv_app.dto.ReservationRequest;
import ee.margus.resto_reserv_app.dto.ReservationResponse;
import ee.margus.resto_reserv_app.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReservationController {
    @Autowired
    private ReservationService service;

    @PostMapping("create-reservation")
    public ReservationResponse createReservation(ReservationRequest reservationRequest){
        return service.create(reservationRequest);
    }
}
