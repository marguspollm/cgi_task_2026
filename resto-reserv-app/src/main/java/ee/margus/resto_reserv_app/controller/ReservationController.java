package ee.margus.resto_reserv_app.controller;

import ee.margus.resto_reserv_app.dto.ReservationRequest;
import ee.margus.resto_reserv_app.dto.ReservationResponse;
import ee.margus.resto_reserv_app.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReservationController {
    @Autowired
    private ReservationService service;

    @PostMapping("create-reservation")
    public ReservationResponse createReservation(@Valid @RequestBody ReservationRequest reservationRequest){
        return service.create(reservationRequest);
    }

    @GetMapping("reservations")
    public List<ReservationResponse> getAllReservations(){
        return service.getAllReservations();
    }

    @GetMapping("end-reservation")
    public void endReservation(@RequestParam Long reservationId){
        service.endReservation(reservationId);
    }

}
