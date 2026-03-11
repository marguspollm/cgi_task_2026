package ee.margus.resto_reserv_app.repository;

import ee.margus.resto_reserv_app.model.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationRepositoryTest {
    private ReservationRepository reservationRepository;

    @BeforeEach
    void setUp(){
        reservationRepository = new ReservationRepository();
    }

    @Test
    void givenReservation_whenSave_thenSaveReservation(){
        Reservation reservation = new Reservation();
        reservation.setDate(LocalDate.now());
        reservation.setTime(LocalTime.now());

        Reservation saved = reservationRepository.save(reservation);

        assertEquals(1L, saved.getId());
        assertEquals(1, reservationRepository.findAll().size());
    }

    @Test
    void whenFindAllReservations_thnReturnReservationList(){
        Reservation r1 = new Reservation();
        Reservation r2 = new Reservation();

        reservationRepository.save(r1);
        reservationRepository.save(r2);

        List<Reservation> reservations = reservationRepository.findAll();

        assertEquals(2, reservations.size());
    }
}