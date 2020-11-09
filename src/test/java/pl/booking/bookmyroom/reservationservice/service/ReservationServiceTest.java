package pl.booking.bookmyroom.reservationservice.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.booking.bookmyroom.hotelservice.model.Hotel;
import pl.booking.bookmyroom.hotelservice.model.RoomStandard;
import pl.booking.bookmyroom.hotelservice.model.RoomType;
import pl.booking.bookmyroom.hotelservice.repository.RoomRepository;
import pl.booking.bookmyroom.hotelservice.service.HotelService;
import pl.booking.bookmyroom.reservationservice.exceptions.MakeReservationException;
import pl.booking.bookmyroom.reservationservice.model.Reservation;
import pl.booking.bookmyroom.reservationservice.model.ReservationStatus;
import pl.booking.bookmyroom.reservationservice.model.requests.MakeReservationRequest;
import pl.booking.bookmyroom.reservationservice.model.responses.MakeReservationResponse;
import pl.booking.bookmyroom.reservationservice.repository.ReservationRepository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    private Reservation reservation;
    private RoomType roomType;
    private Hotel hotel;

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private HotelService hotelService;

    @BeforeEach
    void setUp() throws ParseException {
        reservation = new Reservation();
        reservation.setReservationStart(
                new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020")
        );
        reservation.setReservationEnd(
                new SimpleDateFormat("dd/MM/yyyy").parse("02/01/2020")
        );
        reservation.setHotelsId(1);
        reservation.setRoomTypeId(1);
        reservation.setUserId(1);
        reservation.setFullReservationPrice(23.99f);
        reservation.setStatus(ReservationStatus.PENDING);

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
    void tearDown() {
    }

    @Test
    void makeReservation_Created() throws MakeReservationException, ParseException {
        roomRepository.save(roomType);
        MakeReservationRequest request = new MakeReservationRequest(
                new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"),
                new SimpleDateFormat("dd/MM/yyyy").parse("02/01/2020"),
                1,
                1,
                1
        );
        MakeReservationResponse expectedResponse = new MakeReservationResponse(
                request,
                "Reservation successful"
        );
        MakeReservationResponse actualResponse = reservationService.makeReservation(request);
        assertEquals(expectedResponse.getStartDate(), actualResponse.getStartDate());
        assertEquals(expectedResponse.getEndDate(), actualResponse.getEndDate());
        assertEquals(expectedResponse.getIdHotel(), actualResponse.getIdHotel());
        assertEquals(expectedResponse.getIdRoom(), actualResponse.getIdRoom());
        assertEquals(expectedResponse.getIdUser(), actualResponse.getIdUser());
        assertEquals(expectedResponse.getStatus(), actualResponse.getStatus());
        assertEquals(reservation.getFullReservationPrice(), reservationRepository.findAll().get(0).getFullReservationPrice());
    }

    @Test
    void hotelHasFreeRooms() {
    }

    @Test
    void deleteReservation() {
    }

    @Test
    void getUserReservations() {
    }

    @Test
    void getHotelReservation() {
    }

    @Test
    void getCorporationReservations() {
    }

    @Test
    void editReservation() {
    }

    @Test
    void changeReservationStatus() {
    }
}