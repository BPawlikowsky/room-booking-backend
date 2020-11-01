package pl.booking.bookmyroom.hotelservice.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.booking.bookmyroom.hotelservice.exceptions.CreateHotelException;
import pl.booking.bookmyroom.hotelservice.exceptions.DeleteHotelException;
import pl.booking.bookmyroom.hotelservice.exceptions.FindHotelException;
import pl.booking.bookmyroom.hotelservice.model.Hotel;
import pl.booking.bookmyroom.hotelservice.model.RoomStandard;
import pl.booking.bookmyroom.hotelservice.model.RoomType;
import pl.booking.bookmyroom.hotelservice.model.requests.AddRoomsToHotelRequest;
import pl.booking.bookmyroom.hotelservice.model.requests.CreateHotelRequest;
import pl.booking.bookmyroom.hotelservice.model.requests.DeleteHotelRequest;
import pl.booking.bookmyroom.hotelservice.model.responses.CreateHotelResponse;
import pl.booking.bookmyroom.hotelservice.model.responses.DeleteHotelResponse;
import pl.booking.bookmyroom.hotelservice.model.responses.GetHotelResponse;
import pl.booking.bookmyroom.hotelservice.repository.HotelRepository;
import pl.booking.bookmyroom.hotelservice.repository.RoomRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class HotelServiceTest {
    private Hotel hotel;
    private RoomType roomType;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private HotelService hotelService;
    @Autowired
    private RoomRepository roomRepository;

    @BeforeEach
    void setUp() {
        hotel = new Hotel();
        hotel.setCity("Gotham");
        hotel.setName("Testing Hotel");
        hotel.setStreet("Testing St.");
        hotel.setStreetNumber("10");
        hotel.setPhoneNumber("900800700");
        hotel.setStandard(3);
        hotel.setCorporationId(1);

        roomType = new RoomType();
        roomType.setNumberOfBeds(2);
        roomType.setNumberOfRooms(4);
        roomType.setPrice(23.99f);
        roomType.setStandard(RoomStandard.STANDARD);
    }

    @Test
    void createHotel_Created() throws CreateHotelException {
        CreateHotelRequest request = new CreateHotelRequest(
                hotel.getCity(),
                hotel.getName(),
                hotel.getStreet(),
                hotel.getStreetNumber(),
                hotel.getPhoneNumber(),
                hotel.getStandard(),
                hotel.getCorporationId(),
                new AddRoomsToHotelRequest[] {
                        new AddRoomsToHotelRequest(
                                roomType.getPrice(),
                                roomType.getNumberOfBeds(),
                                roomType.getStandard(),
                                roomType.getNumberOfRooms()
                        )
                }
        );
        CreateHotelResponse expectedResponse = new CreateHotelResponse(
                request.getCity(),
                request.getName(),
                request.getCorporationId(),
                "Hotel successfully created"
        );
        CreateHotelResponse actualResponse = hotelService.createHotel(request);
        assertEquals(expectedResponse.getName(), actualResponse.getName());
        assertEquals(expectedResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedResponse.getCity(), actualResponse.getCity());
        assertEquals(expectedResponse.getCorporationId(), actualResponse.getCorporationId());
        assertFalse(hotelRepository.findByNameAndCity(hotel.getName(), hotel.getCity()).isEmpty());
        hotel.setId(hotelRepository.findAll().get(0).getId());
        roomType.setHotelId(hotelRepository.findAll().get(0).getId());
        hotelRepository.delete(hotel);
        roomType.setId(roomRepository.findAll().get(0).getId());
        roomRepository.delete(roomType);
    }

    @Test
    void createHotel_Hotel_name_in_City_already_exists() {
        hotelRepository.save(hotel);
        CreateHotelRequest request = new CreateHotelRequest(
                hotel.getCity(),
                hotel.getName(),
                hotel.getStreet(),
                hotel.getStreetNumber(),
                hotel.getPhoneNumber(),
                hotel.getStandard(),
                hotel.getCorporationId(),
                new AddRoomsToHotelRequest[] {
                        new AddRoomsToHotelRequest(
                                roomType.getPrice(),
                                roomType.getNumberOfBeds(),
                                roomType.getStandard(),
                                roomType.getNumberOfRooms()
                        )
                }
        );
        assertThrows(CreateHotelException.class, () -> hotelService.createHotel(request));
        hotelRepository.delete(hotel);
    }

    @Test
    void deleteHotel_Ok() throws DeleteHotelException {
        hotelRepository.save(hotel);
        DeleteHotelRequest request = new DeleteHotelRequest(
                hotelRepository.findAll().get(0).getId()
        );
        DeleteHotelResponse expectedResponse = new DeleteHotelResponse(
                request.getId(),
                "Hotel with id " + request.getId() + " deleted"
        );
        DeleteHotelResponse actualResponse = hotelService.deleteHotel(request);
        assertEquals(expectedResponse.getId(), actualResponse.getId());
        assertEquals(expectedResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedResponse.getClass(), actualResponse.getClass());
        hotelRepository.delete(hotel);
    }

    @Test
    void deleteHotel_Not_found_hotel() {
        DeleteHotelRequest request = new DeleteHotelRequest(
                1
        );
        assertThrows(DeleteHotelException.class, () -> hotelService.deleteHotel(request));
    }

    @Test
    void getAllHotels_FOUND() throws FindHotelException {
        hotelRepository.save(hotel);
        roomType.setHotelId(hotel.getId());
        roomRepository.save(roomType);
        List<GetHotelResponse> expectedResponse = List.of(
                new GetHotelResponse(hotel, List.of(new RoomType(
                    1,
                    roomType.getPrice(),
                    roomType.getNumberOfBeds(),
                    roomType.getStandard(),
                    roomType.getNumberOfRooms(),
                    hotel.getId())
                    )
                )
        );
        List<GetHotelResponse> actualResponse = hotelService.getAllHotels();
        assertEquals(expectedResponse.get(0).getHotel(), actualResponse.get(0).getHotel());
        assertEquals(expectedResponse.get(0).getHotelRoomTypes().get(0), actualResponse.get(0).getHotelRoomTypes().get(0));
        roomRepository.delete(roomType);
        hotelRepository.delete(hotel);
    }

    @Test
    public void getAllHotels_NOT_FOUND() {
        assertThrows(FindHotelException.class, () -> hotelService.getAllHotels());
    }

    @Test
    void getHotelsByCity() {
    }

    @Test
    void getHotelsByCorporationId() {
    }

    @Test
    void getHotelsByStandard() {
    }

    @Test
    void findHotelsMatchingQuery() {
    }

    @Test
    void editHotel() {
    }

    @Test
    void getNumberOfRoomsByRoomTypeId() {
    }

    @Test
    void getRoomPriceByRoomTypeId() {
    }
}