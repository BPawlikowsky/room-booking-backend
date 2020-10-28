package pl.booking.bookmyroom.reservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.booking.bookmyroom.reservation.model.*;
import pl.booking.bookmyroom.reservation.service.ReservationService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/reservation")
@CrossOrigin
public class ReservationController {
    private final ReservationService service;

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
    public ResponseEntity<String> deleteReservation(@RequestParam @Valid Integer reservationId){
        if(!service.deleteReservation(reservationId))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        else
            return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    @ResponseStatus(code = HttpStatus.OK)
    public boolean editReservation(@RequestBody @Valid EditReservationRequest request){
        return service.editReservation(request);
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<Reservation> getReservationsByUserId(@RequestParam @Valid Integer userId){
        return service.getUserReservations(userId);
    }

    @GetMapping(value = "/hotel")
    @ResponseStatus(code = HttpStatus.OK)
    public List<Reservation> getReservationsByHotelsId(@RequestParam @Valid Integer hotelsId){
        return service.getHotelReservation(hotelsId);
    }

    @GetMapping(value = "/corporation")
    @ResponseStatus(code = HttpStatus.OK)
    public List<Reservation> getReservationsByCorporationId(@RequestParam @Valid Integer corporationId){
        return service.getCorporationReservations(corporationId);
    }

    @PatchMapping(value = "/status")
    public ResponseEntity<String> changeReservationStatus(@RequestBody @Valid ChangeStatusRequest request){
        if(service.changeReservationStatus(request)){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
