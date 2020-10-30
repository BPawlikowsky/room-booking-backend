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
import pl.booking.bookmyroom.hotelservice.model.Hotel;
import pl.booking.bookmyroom.hotelservice.model.RoomStandard;
import pl.booking.bookmyroom.hotelservice.model.requests.AddRoomsToHotelRequest;
import pl.booking.bookmyroom.hotelservice.model.requests.CreateHotelRequest;
import pl.booking.bookmyroom.hotelservice.model.requests.DeleteHotelRequest;
import pl.booking.bookmyroom.hotelservice.model.responses.CreateHotelResponse;
import pl.booking.bookmyroom.hotelservice.model.responses.DeleteHotelResponse;
import pl.booking.bookmyroom.hotelservice.repository.HotelRepository;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class HotelServiceTest {
    private Hotel hotel;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private HotelService hotelService;

    @BeforeEach
    void setUp() {
        hotel = new Hotel();
        hotel.setId(1);
        hotel.setCity("Gotham");
        hotel.setName("Testing Hotel");
        hotel.setStreet("Testing St.");
        hotel.setStreetNumber("10");
        hotel.setPhoneNumber("900800700");
        hotel.setStandard(3);
        hotel.setCorporationId(1);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createHotel_Created() throws CreateHotelException {
        CreateHotelRequest request = new CreateHotelRequest(
                "Gotham",
                "Testing Hotel",
                "Testing St.",
                "10",
                "900800700",
                3,
                1,
                new AddRoomsToHotelRequest[] {
                        new AddRoomsToHotelRequest(49.99f, 1, RoomStandard.STANDARD, 3),
                        new AddRoomsToHotelRequest(23.99f, 2, RoomStandard.STANDARD, 4),
                        new AddRoomsToHotelRequest(12.99f, 4, RoomStandard.STANDARD, 5)
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
    }

    @Test
    void createHotel_Hotel_name_in_City_already_exists() {
        hotelRepository.save(hotel);
        CreateHotelRequest request = new CreateHotelRequest(
                "Gotham",
                "Testing Hotel",
                "Testing St.",
                "10",
                "900800700",
                3,
                1,
                new AddRoomsToHotelRequest[] {
                        new AddRoomsToHotelRequest(49.99f, 1, RoomStandard.STANDARD, 3),
                        new AddRoomsToHotelRequest(23.99f, 2, RoomStandard.STANDARD, 4),
                        new AddRoomsToHotelRequest(12.99f, 4, RoomStandard.STANDARD, 5)
                }
        );
        assertThrows(CreateHotelException.class, () -> hotelService.createHotel(request));
    }

    @Test
    void deleteHotel_Ok() throws DeleteHotelException {
        hotelRepository.save(hotel);
        DeleteHotelRequest request = new DeleteHotelRequest(
                1
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
    void deleteHotel_Not_found_hotel() throws DeleteHotelException {
        DeleteHotelRequest request = new DeleteHotelRequest(
                1
        );
        assertThrows(DeleteHotelException.class, () -> hotelService.deleteHotel(request));
    }

    @Test
    void getAllHotels() {
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