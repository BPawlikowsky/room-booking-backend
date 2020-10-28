package pl.booking.bookmyroom.hotel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.booking.bookmyroom.hotel.model.*;
import pl.booking.bookmyroom.hotel.service.HotelService;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/hotels")
@CrossOrigin
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public void addHotel(@RequestBody @Valid CreateHotelRequest request) {
        hotelService.addHotel(request);
    }

    @DeleteMapping
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteHotel(@RequestBody @Valid DeleteHotelRequest request) {
        hotelService.deleteHotel(request);
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<GetHotelResponse> getAllHotels() {
        return hotelService.getAllHotels();
    }

    @GetMapping("city/{city}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<Hotel> findHotelByCity(@PathVariable String city) {
        return hotelService.getHotelsByCity(city);
    }

    @GetMapping("company-id/{corporationId}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<Hotel> findHotelByCorporationId(@PathVariable Integer corporationId){
        return hotelService.getHotelsByCorporationId(corporationId);
    }

    @GetMapping("query")
    @ResponseStatus(code = HttpStatus.OK)
    public List<GetHotelResponse> findHotelsBySearchQuery(@RequestParam String city,
                                               @RequestParam(required = false) @Valid @Min(1) @Max(5) Integer hotelStandard,
                                               @RequestParam(required = false) Float priceMin,
                                               @RequestParam(required = false) Float priceMax,
                                               @RequestParam(required = false) Integer numberOfBeds,
                                               @RequestParam(required = false) RoomStandard roomStandard,
                                               @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date start,
                                               @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date end){
        return hotelService.findHotelsMatchingQuery(city, hotelStandard, priceMin, priceMax, numberOfBeds, roomStandard, start, end);
    }

    @PutMapping
    @ResponseStatus(code = HttpStatus.OK)
    public void editHotel(@RequestBody EditHotelRequest request, @RequestParam Integer id) {
        hotelService.editHotel(request, id);
    }
}
