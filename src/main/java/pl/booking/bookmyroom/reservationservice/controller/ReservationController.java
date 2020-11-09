package pl.booking.bookmyroom.reservationservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.booking.bookmyroom.hotelservice.exceptions.FindHotelException;
import pl.booking.bookmyroom.reservationservice.exceptions.DeleteReservationException;
import pl.booking.bookmyroom.reservationservice.exceptions.EditReservationException;
import pl.booking.bookmyroom.reservationservice.exceptions.MakeReservationException;
import pl.booking.bookmyroom.reservationservice.model.*;
import pl.booking.bookmyroom.reservationservice.model.requests.ChangeStatusRequest;
import pl.booking.bookmyroom.reservationservice.model.requests.EditReservationRequest;
import pl.booking.bookmyroom.reservationservice.model.requests.MakeReservationRequest;
import pl.booking.bookmyroom.reservationservice.model.responses.DeleteReservationResponse;
import pl.booking.bookmyroom.reservationservice.model.responses.EditReservationResponse;
import pl.booking.bookmyroom.reservationservice.model.responses.MakeReservationResponse;
import pl.booking.bookmyroom.reservationservice.service.ReservationService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/reservations")
@CrossOrigin
public class ReservationController {
    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<MakeReservationResponse> makeNewReservation(@RequestBody @Valid MakeReservationRequest request)
            throws MakeReservationException {
        MakeReservationResponse response = service.makeReservation(request);
        if(response.getStatus().equals("Reservation successful"))
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        else
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping
    public ResponseEntity<DeleteReservationResponse> deleteReservation(@RequestParam @Valid Integer reservationId)
            throws DeleteReservationException {
        DeleteReservationResponse response = service.deleteReservation(reservationId);
        if(response.getStatus().equals("Reservation successfully deleted"))
            return new ResponseEntity<>(response, HttpStatus.OK);
        else
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @PutMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<EditReservationResponse> editReservation(@RequestBody @Valid EditReservationRequest request)
            throws EditReservationException {
        EditReservationResponse response = service.editReservation(request);
        if(response.getStatus().equals("Reservation edited successfully"))
            return new ResponseEntity<>(response, HttpStatus.OK);
        else
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/userId/{userId}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<Reservation> getReservationsByUserId(@RequestParam @Valid Integer userId){
        return service.getUserReservations(userId);
    }

    @GetMapping(value = "/hotelId/{hotelId}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<Reservation> getReservationsByHotelsId(@RequestParam @Valid Integer hotelsId){
        return service.getHotelReservation(hotelsId);
    }

    @GetMapping(value = "/corporationId/{corporationId}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<Reservation> getReservationsByCorporationId(@RequestParam @Valid Integer corporationId) throws FindHotelException {
        return service.getCorporationReservations(corporationId);
    }

    @PatchMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<String> changeReservationStatus(@RequestBody @Valid ChangeStatusRequest request){
        if(service.changeReservationStatus(request)){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
