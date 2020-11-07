package pl.booking.bookmyroom.hotelservice.service;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.booking.bookmyroom.hotelservice.exceptions.CreateHotelException;
import pl.booking.bookmyroom.hotelservice.exceptions.DeleteHotelException;
import pl.booking.bookmyroom.hotelservice.exceptions.EditHotelException;
import pl.booking.bookmyroom.hotelservice.exceptions.FindHotelException;
import pl.booking.bookmyroom.hotelservice.model.responses.DeleteHotelResponse;
import pl.booking.bookmyroom.hotelservice.model.*;
import pl.booking.bookmyroom.hotelservice.model.requests.AddRoomsToHotelRequest;
import pl.booking.bookmyroom.hotelservice.model.requests.CreateHotelRequest;
import pl.booking.bookmyroom.hotelservice.model.requests.DeleteHotelRequest;
import pl.booking.bookmyroom.hotelservice.model.requests.EditHotelRequest;
import pl.booking.bookmyroom.hotelservice.model.responses.CreateHotelResponse;
import pl.booking.bookmyroom.hotelservice.model.responses.EditHotelResponse;
import pl.booking.bookmyroom.hotelservice.model.responses.GetHotelResponse;
import pl.booking.bookmyroom.hotelservice.repository.HotelRepository;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private RoomService roomService;

    public HotelService(HotelRepository hotelRepository, RoomService roomService) {
        this.hotelRepository = hotelRepository;
        this.roomService = roomService;
    }

    public CreateHotelResponse createHotel(CreateHotelRequest request) throws CreateHotelException {
        Hotel hotel;
        String status;
        if(request.getName().equals("") || request.getCity().equals("")) {
            status = "No hotel or city name";
            throw new CreateHotelException(status);
        }
        else if(request.getStreet().equals("") || request.getStreetNumber().equals("")) {
            status = "No Street name or no street number";
            throw new CreateHotelException(status);
        }
        else if(!hotelRepository.findByNameAndCity(request.getName(), request.getCity()).isEmpty()) {
            status = "Hotel by the name " + request.getName() + " in " + request.getCity() + " city already exists.";
            throw new CreateHotelException(status);
        }
        else {
            hotel = new Hotel();
            hotel.setName(request.getName());
            hotel.setCity(request.getCity());
            hotel.setStreet(request.getStreet());
            hotel.setStreetNumber(request.getStreetNumber());
            hotel.setPhoneNumber(request.getPhoneNumber());
            hotel.setStandard(request.getStandard());
            hotel.setCorporationId(request.getCorporationId());
            hotelRepository.save(hotel);

            AddRoomsToHotelRequest[] roomsToHotelRequests = request.getRoomsToHotelRequests();
            for (AddRoomsToHotelRequest r : roomsToHotelRequests) {
                roomService.addRoomsToHotel(r, hotel.getId());
            }
            status = "Hotel successfully created";
        }
        return new CreateHotelResponse(request, status);
    }

    public DeleteHotelResponse deleteHotel(DeleteHotelRequest request) throws DeleteHotelException {
        String status;
        if(hotelRepository.findById(request.getId()).isPresent()) {
            hotelRepository.deleteById(request.getId());
            status = "Hotel with id " + request.getId() + " deleted";
        } else {
            status = "Hotel with id " + request.getId() + " not found";
            throw new DeleteHotelException(status);
        }
        return new DeleteHotelResponse(
                request.getId(),
                status
        );
    }

    public List<GetHotelResponse> getAllHotels() throws FindHotelException {
        List<Hotel> hotels = hotelRepository.findAll();
        List<RoomType> roomTypes = roomService.getAllRoomTypes();
        List<GetHotelResponse> response = new ArrayList<>();
        for (Hotel h : hotels) {
            List<RoomType> hotelRooms =
                    roomTypes.stream()
                            .filter(
                                    r -> r.getHotelId().equals(h.getId())).collect(Collectors.toList()
                    );
            response.add(new GetHotelResponse(h, hotelRooms));
        }
        if(response.isEmpty())
            throw new FindHotelException("No hotels found");
        return response;
    }

    public List<Hotel> getHotelsByCity(String city) throws FindHotelException {
        List<Hotel> responses = hotelRepository.findByCity(city);
        if(responses.isEmpty())
            throw new FindHotelException("City not found");
        return responses;
    }

    public List<Hotel> getHotelsByCorporationId(Integer corporationId) throws FindHotelException {
        List<Hotel> responses = hotelRepository.findHotelByCorporationId(corporationId);
        if(responses.isEmpty())
            throw new FindHotelException("Corporation id " + corporationId + ": No hotels found");
        return responses;
    }

    public List<Hotel> getHotelsByStandard(Integer standard) throws FindHotelException {
        List<Hotel> responses = hotelRepository.findByStandard(standard);
        if(responses.isEmpty())
            throw new FindHotelException("No hotels of standard " + standard + " found");
        return responses;
    }

    private List<Hotel> getHotelsByStandardAndCity(Integer standard, String city) throws FindHotelException {
        List<Hotel> responses = hotelRepository.findByStandardAndCity(standard, city);
        if(responses.isEmpty())
            throw new FindHotelException("No hotels of standard " + standard + " found in " + city + " city");
        return responses;
    }

    public List<GetHotelResponse> findHotelsMatchingQuery(String city,
                                               Integer hotelStandard,
                                               Float priceMin,
                                               Float priceMax,
                                               Integer numberOfBeds,
                                               RoomStandard roomStandard,
                                               Date start,
                                               Date end
    ) throws FindHotelException {
        List<GetHotelResponse> searchResult = new ArrayList<>();
        if(city != null && hotelStandard != null) {
            for (Hotel h : getHotelsByStandardAndCity(hotelStandard, city)){
                GetHotelResponse r = new GetHotelResponse();
                r.setHotel(h);
                searchResult.add(r);
            }
        }
        else if(hotelStandard != null){
            for (Hotel h : getHotelsByStandard(hotelStandard)){
                GetHotelResponse r = new GetHotelResponse();
                r.setHotel(h);
                searchResult.add(r);
            }
        }  else {
            List<RoomType> rooms = roomService.anyRoomsMatchQuery(
                    null,
                    numberOfBeds != null ? Optional.of(numberOfBeds) : Optional.empty(),
                    roomStandard != null ? Optional.of(roomStandard) : Optional.empty(),
                    priceMin != null ? Optional.of(priceMin) : Optional.empty(),
                    priceMax != null ? Optional.of(priceMax) : Optional.empty(),
                    start != null ? Optional.of(start) : Optional.empty(),
                    end != null ? Optional.of(end) : Optional.empty()
            );

            if(!rooms.isEmpty()) {
                Hotel hotelByRoom = new Hotel();
                hotelRepository.findById(rooms.get(0).getHotelId()).ifPresent(
                        hotel -> {
                            hotelByRoom.setCorporationId(hotel.getCorporationId());
                        }
                );
                for (Hotel h : getHotelsByCorporationId(hotelByRoom.getCorporationId())) {
                    GetHotelResponse r = new GetHotelResponse();
                    r.setHotel(h);
                    searchResult.add(r);
                }
            }
        }

        searchResult = searchResult.stream()
                .filter(r -> !roomService.anyRoomsMatchQuery(
                        r.getHotel().getId(),
                        Optional.ofNullable(numberOfBeds),
                        Optional.ofNullable(roomStandard),
                        Optional.ofNullable(priceMin),
                        Optional.ofNullable(priceMax),
                        Optional.ofNullable(start),
                        Optional.ofNullable(end)
                ).isEmpty())
                .collect(Collectors.toList());

        searchResult.forEach(r -> r.setHotelRoomTypes(roomService.getRoomTypesByHotelsId(r.getHotel().getId())));

        if(searchResult.size() > 0)
            return searchResult;
        else
            throw new FindHotelException("Query didn't find hotels with given parameters");
    }

    public EditHotelResponse editHotel(EditHotelRequest request, Integer id) throws EditHotelException {
        String status;
        Optional<Hotel> hotel = hotelRepository.findById(id);
        if(hotel.isPresent()) {
            Hotel hotelToEdit = hotel.get();
            if (request.getPhoneNumber() != null) {
                hotelToEdit.setPhoneNumber(request.getPhoneNumber());
            }
            if (request.getStandard() != null) {
                hotelToEdit.setStandard(request.getStandard());
            }
            hotelRepository.save(hotelToEdit);
            status = "Hotel edited successfully";
        } else {
            status = "Hotel not found";
            throw new EditHotelException(status);
        }
        return new EditHotelResponse(request, status);
    }

    public Integer getNumberOfRoomsByRoomTypeId(Integer id) {
        return roomService.getNumberOfRoomsById(id);
    }

    public Float getRoomPriceByRoomTypeId(Integer id) {
        return roomService.getRoomTypeById(id).getPrice();
    }
}