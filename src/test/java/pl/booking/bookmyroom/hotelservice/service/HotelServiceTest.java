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
import pl.booking.bookmyroom.hotelservice.exceptions.EditHotelException;
import pl.booking.bookmyroom.hotelservice.exceptions.FindHotelException;
import pl.booking.bookmyroom.hotelservice.model.Hotel;
import pl.booking.bookmyroom.hotelservice.model.RoomStandard;
import pl.booking.bookmyroom.hotelservice.model.RoomType;
import pl.booking.bookmyroom.hotelservice.model.requests.AddRoomsToHotelRequest;
import pl.booking.bookmyroom.hotelservice.model.requests.CreateHotelRequest;
import pl.booking.bookmyroom.hotelservice.model.requests.DeleteHotelRequest;
import pl.booking.bookmyroom.hotelservice.model.requests.EditHotelRequest;
import pl.booking.bookmyroom.hotelservice.model.responses.CreateHotelResponse;
import pl.booking.bookmyroom.hotelservice.model.responses.DeleteHotelResponse;
import pl.booking.bookmyroom.hotelservice.model.responses.EditHotelResponse;
import pl.booking.bookmyroom.hotelservice.model.responses.GetHotelResponse;
import pl.booking.bookmyroom.hotelservice.repository.HotelRepository;
import pl.booking.bookmyroom.hotelservice.repository.RoomRepository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
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
        roomType.setHotelId(hotel.getId());
        roomType.setNumberOfBeds(2);
        roomType.setNumberOfRooms(4);
        roomType.setPrice(23.99f);
        roomType.setStandard(RoomStandard.STANDARD);
    }
    
    @AfterEach
    public void Cleanup() {
        hotelRepository.delete(hotel);
        roomRepository.delete(roomType);
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
    }

    @Test
    void deleteHotel_Not_found_hotel() {
        DeleteHotelRequest request = new DeleteHotelRequest(
                1
        );
        assertThrows(DeleteHotelException.class, () -> hotelService.deleteHotel(request));
    }

    @Test
    void getAllHotels_Found() throws FindHotelException {
        hotelRepository.save(hotel);
        roomType.setHotelId(hotel.getId());
        roomRepository.save(roomType);
        List<GetHotelResponse> expectedResponse = List.of(
                new GetHotelResponse(hotel, List.of(new RoomType(
                        roomType.getId(),
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
        hotel.setId(hotelRepository.findAll().get(0).getId());
        hotelRepository.delete(hotel);
    }

    @Test
    public void getAllHotels_Not_found() {
        assertThrows(FindHotelException.class, () -> hotelService.getAllHotels());
    }

    @Test
    void getHotelsByCity_Found() throws FindHotelException {
        hotelRepository.save(hotel);
        List<Hotel> expectedResponse = List.of(hotel);
        List<Hotel> actualResponse = hotelService.getHotelsByCity(hotel.getCity());
        assertEquals(expectedResponse.get(0), actualResponse.get(0));
        hotel.setId(hotelRepository.findAll().get(0).getId());
        hotelRepository.delete(hotel);
    }

    @Test
    void getHotelsByCity_Not_found() {
        assertThrows(FindHotelException.class, () -> hotelService.getHotelsByCity("Test city"));
    }

    @Test
    void getHotelsByCorporationId_Found() throws FindHotelException {
        hotelRepository.save(hotel);
        List<Hotel> expectedResponse = List.of(hotel);
        List<Hotel> actualResponse = hotelService.getHotelsByCorporationId(hotel.getCorporationId());
        assertEquals(expectedResponse.get(0), actualResponse.get(0));
        hotel.setId(hotelRepository.findAll().get(0).getId());
        hotelRepository.delete(hotel);
    }

    @Test
    void getHotelsByCorporationId_Not_found() {
        assertThrows(FindHotelException.class, () -> hotelService.getHotelsByCorporationId(1));
    }

    @Test
    void getHotelsByStandard_Found() throws FindHotelException {
        hotelRepository.save(hotel);
        List<Hotel> expectedResponse = List.of(hotel);
        List<Hotel> actualResponse = hotelService.getHotelsByStandard(hotel.getStandard());
        assertEquals(expectedResponse.get(0), actualResponse.get(0));
        hotel.setId(hotelRepository.findAll().get(0).getId());
        hotelRepository.delete(hotel);
    }

    @Test
    void getHotelsByStandard_Not_Found() {
        assertThrows(FindHotelException.class, () -> hotelService.getHotelsByStandard(3));
    }

    @Test
    void findHotelsMatchingQuery_Found_All() throws FindHotelException {
        hotelRepository.save(hotel);
        roomType.setHotelId(hotel.getId());
        roomRepository.save(roomType);
        List<GetHotelResponse> expectedResponse = List.of(new GetHotelResponse(
                hotel,
                List.of(roomType)
        ));
        List<GetHotelResponse> actualResponse = hotelService.findHotelsMatchingQuery(
                hotel.getCity(),
                hotel.getStandard(),
                0.0f,
                99.9f,
                roomType.getNumberOfBeds(),
                RoomStandard.STANDARD,
                DateFormat.getDateInstance().parse("01/01/00 0:00 AM, CET", new ParsePosition(0)),
                DateFormat.getDateInstance().parse("31/12/20 12:59 PM, CET", new ParsePosition(0))
        );
        
        assertEquals(expectedResponse.get(0).getHotel(), actualResponse.get(0).getHotel());
        assertEquals(expectedResponse.get(0).getHotelRoomTypes(), actualResponse.get(0).getHotelRoomTypes());
    }

    @Test
    void findHotelsMatchingQuery_Found_Name() throws FindHotelException {
        hotelRepository.save(hotel);
        roomType.setHotelId(hotel.getId());
        roomRepository.save(roomType);
        List<GetHotelResponse> expectedResponse = List.of(new GetHotelResponse(
                hotel,
                List.of(roomType)
        ));
        List<GetHotelResponse> actualResponse = hotelService.findHotelsMatchingQuery(
                hotel.getCity(),
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
        
        assertEquals(expectedResponse.get(0).getHotel(), actualResponse.get(0).getHotel());
        assertEquals(expectedResponse.get(0).getHotelRoomTypes(), actualResponse.get(0).getHotelRoomTypes());
    }

    @Test
    void findHotelsMatchingQuery_Found_HotelStandard() throws FindHotelException {
        hotelRepository.save(hotel);
        roomType.setHotelId(hotel.getId());
        roomRepository.save(roomType);
        List<GetHotelResponse> expectedResponse = List.of(new GetHotelResponse(
                hotel,
                List.of(roomType)
        ));
        List<GetHotelResponse> actualResponse = hotelService.findHotelsMatchingQuery(
                null,
                3,
                null,
                null,
                null,
                null,
                null,
                null
        );
        
        assertEquals(expectedResponse.get(0).getHotel(), actualResponse.get(0).getHotel());
        assertEquals(expectedResponse.get(0).getHotelRoomTypes(), actualResponse.get(0).getHotelRoomTypes());
    }

    @Test
    void findHotelsMatchingQuery_Found_Price() throws FindHotelException {
        hotelRepository.save(hotel);
        roomType.setHotelId(hotel.getId());
        roomRepository.save(roomType);
        List<GetHotelResponse> expectedResponse = List.of(new GetHotelResponse(
                hotel,
                List.of(roomType)
        ));
        List<GetHotelResponse> actualResponse = hotelService.findHotelsMatchingQuery(
                null,
                null,
                0.0f,
                99.9f,
                null,
                null,
                null,
                null
        );
        
        assertEquals(expectedResponse.get(0).getHotel(), actualResponse.get(0).getHotel());
        assertEquals(expectedResponse.get(0).getHotelRoomTypes(), actualResponse.get(0).getHotelRoomTypes());
    }

    @Test
    void findHotelsMatchingQuery_Found_NumberOfBeds() throws FindHotelException {
        hotelRepository.save(hotel);
        roomType.setHotelId(hotel.getId());
        roomRepository.save(roomType);
        List<GetHotelResponse> expectedResponse = List.of(new GetHotelResponse(
                hotel,
                List.of(roomType)
        ));
        List<GetHotelResponse> actualResponse = hotelService.findHotelsMatchingQuery(
                null,
                null,
                null,
                null,
                2,
                null,
                null,
                null
        );
        hotelRepository.delete(hotel);
        roomRepository.delete(roomType);
        assertEquals(expectedResponse.get(0).getHotel(), actualResponse.get(0).getHotel());
        assertEquals(expectedResponse.get(0).getHotelRoomTypes(), actualResponse.get(0).getHotelRoomTypes());
    }

    @Test
    void findHotelsMatchingQuery_Found_RoomStandard() throws FindHotelException {
        hotelRepository.save(hotel);
        roomType.setHotelId(hotel.getId());
        roomRepository.save(roomType);
        List<GetHotelResponse> expectedResponse = List.of(new GetHotelResponse(
                hotel,
                List.of(roomType)
        ));
        List<GetHotelResponse> actualResponse = hotelService.findHotelsMatchingQuery(
                null,
                null,
                null,
                null,
                null,
                RoomStandard.STANDARD,
                null,
                null
        );
        
        assertEquals(expectedResponse.get(0).getHotel(), actualResponse.get(0).getHotel());
        assertEquals(expectedResponse.get(0).getHotelRoomTypes(), actualResponse.get(0).getHotelRoomTypes());
    }

    @Test
    void findHotelsMatchingQuery_Found_Date() throws FindHotelException, ParseException {
        hotelRepository.save(hotel);
        roomType.setHotelId(hotel.getId());
        roomRepository.save(roomType);
        List<GetHotelResponse> expectedResponse = List.of(new GetHotelResponse(
                hotel,
                List.of(roomType)
        ));
        List<GetHotelResponse> actualResponse = hotelService.findHotelsMatchingQuery(
                null,
                null,
                null,
                null,
                null,
                null,
                new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1900"),
                new SimpleDateFormat("dd/MM/yyyy").parse("31/12/2020")
        );
        
        assertEquals(expectedResponse.get(0).getHotel(), actualResponse.get(0).getHotel());
        assertEquals(expectedResponse.get(0).getHotelRoomTypes(), actualResponse.get(0).getHotelRoomTypes());
    }

    @Test
    void findHotelsMatchingQuery_Not_Found_City() {
        hotelRepository.save(hotel);
        roomType.setHotelId(hotel.getId());
        roomRepository.save(roomType);
        assertThrows(FindHotelException.class, () -> hotelService.findHotelsMatchingQuery(
                "New York",
                hotel.getStandard(),
                0.0f,
                99.9f,
                roomType.getNumberOfBeds(),
                RoomStandard.STANDARD,
                DateFormat.getDateInstance().parse("01/01/00 0:00 AM, CET", new ParsePosition(0)),
                DateFormat.getDateInstance().parse("31/12/20 12:59 PM, CET", new ParsePosition(0))
        ));
    }

    @Test
    void findHotelsMatchingQuery_Not_Found_HotelStandard() {
        hotelRepository.save(hotel);
        roomType.setHotelId(hotel.getId());
        roomRepository.save(roomType);
        assertThrows(FindHotelException.class, () -> hotelService.findHotelsMatchingQuery(
                hotel.getCity(),
                5,
                0.0f,
                99.9f,
                roomType.getNumberOfBeds(),
                RoomStandard.STANDARD,
                DateFormat.getDateInstance().parse("01/01/00 0:00 AM, CET", new ParsePosition(0)),
                DateFormat.getDateInstance().parse("31/12/20 12:59 PM, CET", new ParsePosition(0))
        ));
    }

    @Test
    void findHotelsMatchingQuery_Not_Found_Prices() {
        hotelRepository.save(hotel);
        roomType.setHotelId(hotel.getId());
        roomRepository.save(roomType);
        assertThrows(FindHotelException.class, () -> hotelService.findHotelsMatchingQuery(
                hotel.getCity(),
                3,
                50.0f,
                99.9f,
                roomType.getNumberOfBeds(),
                RoomStandard.STANDARD,
                DateFormat.getDateInstance().parse("01/01/00 0:00 AM, CET", new ParsePosition(0)),
                DateFormat.getDateInstance().parse("31/12/20 12:59 PM, CET", new ParsePosition(0))
        ));
    }

    @Test
    void findHotelsMatchingQuery_Not_Found_NumberOfBeds() {
        hotelRepository.save(hotel);
        roomType.setHotelId(hotel.getId());
        roomRepository.save(roomType);
        assertThrows(FindHotelException.class, () -> hotelService.findHotelsMatchingQuery(
                hotel.getCity(),
                3,
                0.0f,
                99.9f,
                4,
                RoomStandard.STANDARD,
                DateFormat.getDateInstance().parse("01/01/00 0:00 AM, CET", new ParsePosition(0)),
                DateFormat.getDateInstance().parse("31/12/20 12:59 PM, CET", new ParsePosition(0))
        ));
    }

    @Test
    void findHotelsMatchingQuery_Not_Found_RoomStandard() {
        hotelRepository.save(hotel);
        roomType.setHotelId(hotel.getId());
        roomRepository.save(roomType);
        assertThrows(FindHotelException.class, () -> hotelService.findHotelsMatchingQuery(
                hotel.getCity(),
                3,
                0.0f,
                99.9f,
                4,
                RoomStandard.VIP,
                DateFormat.getDateInstance().parse("01/01/00 0:00 AM, CET", new ParsePosition(0)),
                DateFormat.getDateInstance().parse("31/12/20 12:59 PM, CET", new ParsePosition(0))
        ));
    }

    @Test
    void editHotel_Ok() throws EditHotelException {
        hotelRepository.save(hotel);
        roomType.setHotelId(hotel.getId());
        roomRepository.save(roomType);
        EditHotelRequest request = new EditHotelRequest(
                "900800700",
                3
        );
        EditHotelResponse expectedResponse = new EditHotelResponse(request, "Hotel edited successfully");
        EditHotelResponse actualResponse = hotelService.editHotel(request, hotel.getId());
        assertEquals(expectedResponse.getPhoneNumber(), actualResponse.getPhoneNumber());
        assertEquals(expectedResponse.getStandard(), actualResponse.getStandard());
        assertEquals(expectedResponse.getStatus(), actualResponse.getStatus());
    }

    @Test
    void editHotel_Not_Found() throws EditHotelException {
        EditHotelRequest request = new EditHotelRequest(
                "900800700",
                3
        );
        assertThrows(EditHotelException.class, () -> hotelService.editHotel(request, 1));
    }
}