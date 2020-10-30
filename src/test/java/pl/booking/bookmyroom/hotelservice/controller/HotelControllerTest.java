package pl.booking.bookmyroom.hotelservice.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.booking.bookmyroom.hotelservice.exceptions.CreateHotelException;
import pl.booking.bookmyroom.hotelservice.exceptions.DeleteHotelException;
import pl.booking.bookmyroom.hotelservice.model.RoomStandard;
import pl.booking.bookmyroom.hotelservice.model.requests.AddRoomsToHotelRequest;
import pl.booking.bookmyroom.hotelservice.model.requests.CreateHotelRequest;
import pl.booking.bookmyroom.hotelservice.model.requests.DeleteHotelRequest;
import pl.booking.bookmyroom.hotelservice.model.responses.CreateHotelResponse;
import pl.booking.bookmyroom.hotelservice.model.responses.DeleteHotelResponse;
import pl.booking.bookmyroom.hotelservice.repository.HotelRepository;
import pl.booking.bookmyroom.hotelservice.service.HotelService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class HotelControllerTest {

    @Mock
    private HotelRepository hotelRepository;

    @Spy
    private HotelService hotelService;

    @InjectMocks
    private HotelController hotelController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createHotel_CREATED() throws CreateHotelException {
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
        CreateHotelResponse response = new CreateHotelResponse(
                request.getCity(),
                request.getName(),
                request.getCorporationId(),
                "Hotel successfully created"
        );
        ResponseEntity<CreateHotelResponse> expectedResponse = new ResponseEntity<>(response, HttpStatus.CREATED);
        doReturn(response).when(hotelService).createHotel(request);
        ResponseEntity<CreateHotelResponse> actualResponse = hotelController.createHotel(request);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void createHotel_BAD_REQUEST() throws CreateHotelException {
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
        doThrow(CreateHotelException.class).when(hotelService).createHotel(request);
        assertThrows(CreateHotelException.class, () -> hotelController.createHotel(request));
    }

    @Test
    void deleteHotel_OK() throws DeleteHotelException {
        DeleteHotelRequest request = new DeleteHotelRequest(
                1
        );
        DeleteHotelResponse response = new DeleteHotelResponse(
                request.getId(),
                "Hotel with id " + request.getId() + " deleted"
        );
        ResponseEntity<DeleteHotelResponse> expectedResponse = new ResponseEntity<>(response, HttpStatus.OK);
        doReturn(response).when(hotelService).deleteHotel(request);
        ResponseEntity<DeleteHotelResponse> actualResponse = hotelController.deleteHotel(request);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void deleteHotel_BAD_REQUEST() throws DeleteHotelException {
        DeleteHotelRequest request = new DeleteHotelRequest(
                2
        );
        doThrow(DeleteHotelException.class).when(hotelService).deleteHotel(request);
        assertThrows(DeleteHotelException.class,() -> hotelController.deleteHotel(request));
    }

    @Test
    void getAllHotels() {
    }

    @Test
    void findHotelByCity() {
    }

    @Test
    void findHotelByCorporationId() {
    }

    @Test
    void findHotelsBySearchQuery() {
    }

    @Test
    void editHotel() {
    }
}