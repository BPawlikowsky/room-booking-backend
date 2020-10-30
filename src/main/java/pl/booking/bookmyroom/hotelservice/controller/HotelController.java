package pl.booking.bookmyroom.hotelservice.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.booking.bookmyroom.hotelservice.exceptions.CreateHotelException;
import pl.booking.bookmyroom.hotelservice.exceptions.DeleteHotelException;
import pl.booking.bookmyroom.hotelservice.model.*;
import pl.booking.bookmyroom.hotelservice.model.requests.CreateHotelRequest;
import pl.booking.bookmyroom.hotelservice.model.requests.DeleteHotelRequest;
import pl.booking.bookmyroom.hotelservice.model.requests.EditHotelRequest;
import pl.booking.bookmyroom.hotelservice.model.responses.CreateHotelResponse;
import pl.booking.bookmyroom.hotelservice.model.responses.DeleteHotelResponse;
import pl.booking.bookmyroom.hotelservice.model.responses.GetHotelResponse;
import pl.booking.bookmyroom.hotelservice.service.HotelService;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/hotels")
@CrossOrigin
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<CreateHotelResponse> createHotel(@RequestBody @Valid CreateHotelRequest request) throws CreateHotelException {
        CreateHotelResponse response = hotelService.createHotel(request);
        if(response.getStatus().equals("Hotel successfully created"))
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        else
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<DeleteHotelResponse> deleteHotel(@RequestBody @Valid DeleteHotelRequest request) throws DeleteHotelException {
        DeleteHotelResponse response = hotelService.deleteHotel(request);
        if(response.getStatus().matches("Hotel with id (.*) deleted"))
            return new ResponseEntity<>(response, HttpStatus.OK);
        else
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.FOUND)
    public ResponseEntity<List<GetHotelResponse>> getAllHotels() {
        List<GetHotelResponse> responses = hotelService.getAllHotels();
        if(!responses.isEmpty())
            return new ResponseEntity<>(responses, HttpStatus.FOUND);
        else
            return new ResponseEntity<>(responses, HttpStatus.NOT_FOUND);
    }

    @GetMapping("city/{city}")
    @ResponseStatus(code = HttpStatus.FOUND)
    public ResponseEntity<List<Hotel>> findHotelByCity(@PathVariable String city) {
        List<Hotel> responses =  hotelService.getHotelsByCity(city);
        if(!responses.isEmpty())
            return new ResponseEntity<>(responses, HttpStatus.FOUND);
        else
            return new ResponseEntity<>(responses, HttpStatus.NOT_FOUND);
    }

    @GetMapping("corporation-id/{corporationId}")
    @ResponseStatus(code = HttpStatus.FOUND)
    public ResponseEntity<List<Hotel>> findHotelByCorporationId(@PathVariable Integer corporationId){
        List<Hotel> responses = hotelService.getHotelsByCorporationId(corporationId);
        if(!responses.isEmpty())
            return new ResponseEntity<>(responses, HttpStatus.FOUND);
        else
            return new ResponseEntity<>(responses, HttpStatus.NOT_FOUND);
    }

    @GetMapping("query")
    @ResponseStatus(code = HttpStatus.FOUND)
    public ResponseEntity<List<GetHotelResponse>> findHotelsBySearchQuery(@RequestParam String city,
                                               @RequestParam(required = false) @Valid @Min(1) @Max(5) Integer hotelStandard,
                                               @RequestParam(required = false) Float priceMin,
                                               @RequestParam(required = false) Float priceMax,
                                               @RequestParam(required = false) Integer numberOfBeds,
                                               @RequestParam(required = false) RoomStandard roomStandard,
                                               @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date start,
                                               @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date end){
        List<GetHotelResponse> responses = hotelService.findHotelsMatchingQuery(
                city, hotelStandard,
                priceMin, priceMax,
                numberOfBeds, roomStandard,
                start, end
        );
        if(!responses.isEmpty())
            return new ResponseEntity<>(responses, HttpStatus.FOUND);
        else
            return new ResponseEntity<>(responses, HttpStatus.NOT_FOUND);
    }

    @PutMapping
    @ResponseStatus(code = HttpStatus.OK)
    public void editHotel(@RequestBody EditHotelRequest request, @RequestParam Integer id) {
        hotelService.editHotel(request, id);
    }
}
