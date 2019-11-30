package pl.booking.bookmyroom.reservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.booking.bookmyroom.reservation.model.DeleteReservationRequest;
import pl.booking.bookmyroom.reservation.model.EditReservationRequest;
import pl.booking.bookmyroom.reservation.model.MakeReservationRequest;
import pl.booking.bookmyroom.reservation.service.ReservationService;

import javax.validation.Valid;

@RestController
@RequestMapping("/reservation")
@CrossOrigin(value = "http://localhost:8000")
public class ReservationController {
    private final ReservationService service;

    @Autowired
    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<String> makeNewReservation(@RequestBody @Valid MakeReservationRequest request){
        if(!service.makeReservation(request))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        else
            return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    @ResponseStatus(code = HttpStatus.OK)
    public boolean deleteReservation(@RequestBody @Valid DeleteReservationRequest request){
        return service.deleteReservation(request);
    }

    @PutMapping
    @ResponseStatus(code = HttpStatus.OK)
    public boolean editReservation(@RequestBody @Valid EditReservationRequest request){
        return service.editReservation(request);
    }
}
