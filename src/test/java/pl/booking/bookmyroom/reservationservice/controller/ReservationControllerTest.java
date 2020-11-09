package pl.booking.bookmyroom.reservationservice.controller;

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
import pl.booking.bookmyroom.reservationservice.exceptions.DeleteReservationException;
import pl.booking.bookmyroom.reservationservice.exceptions.MakeReservationException;
import pl.booking.bookmyroom.reservationservice.model.requests.MakeReservationRequest;
import pl.booking.bookmyroom.reservationservice.model.responses.DeleteReservationResponse;
import pl.booking.bookmyroom.reservationservice.model.responses.MakeReservationResponse;
import pl.booking.bookmyroom.reservationservice.repository.ReservationRepository;
import pl.booking.bookmyroom.reservationservice.service.ReservationService;

import java.text.DateFormat;
import java.text.ParsePosition;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ReservationControllerTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Spy
    private ReservationService reservationService;

    @InjectMocks
    private ReservationController reservationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void makeNewReservation_CREATED() throws MakeReservationException {
        MakeReservationRequest request = new MakeReservationRequest(
                DateFormat.getDateInstance().parse("01/01/00 0:00 AM, CET", new ParsePosition(0)),
                DateFormat.getDateInstance().parse("31/12/20 12:59 PM, CET", new ParsePosition(0)),
                1,
                1,
                1
        );
        MakeReservationResponse response = new MakeReservationResponse(request, "Reservation successful");
        ResponseEntity<MakeReservationResponse> expectedResponse = new ResponseEntity<>(response, HttpStatus.CREATED);
        doReturn(response).when(reservationService).makeReservation(request);
        ResponseEntity<MakeReservationResponse> actualResponse = reservationController.makeNewReservation(request);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void makeNewReservation_BAD_REQUEST() throws MakeReservationException {
        MakeReservationRequest request = new MakeReservationRequest(
                DateFormat.getDateInstance().parse("01/01/00 0:00 AM, CET", new ParsePosition(0)),
                DateFormat.getDateInstance().parse("31/12/20 12:59 PM, CET", new ParsePosition(0)),
                1,
                1,
                1
        );
        doThrow(MakeReservationException.class).when(reservationService).makeReservation(request);
        assertThrows(MakeReservationException.class, () -> reservationController.makeNewReservation(request));
    }

    @Test
    void deleteReservation_OK() throws DeleteReservationException {
        Integer request = 1;
        DeleteReservationResponse response = new DeleteReservationResponse(request, "Reservation successfully deleted");
        ResponseEntity<DeleteReservationResponse> expectedResponse = new ResponseEntity<>(response, HttpStatus.OK);
        doReturn(response).when(reservationService).deleteReservation(request);
        ResponseEntity<DeleteReservationResponse> actualResponse = reservationController.deleteReservation(request);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void deleteReservation_NOT_FOUND() throws DeleteReservationException {
        Integer request = 1;
        DeleteReservationResponse response = new DeleteReservationResponse(request, "Reservation successfully deleted");
        doThrow(DeleteReservationException.class).when(reservationService).deleteReservation(request);
        assertThrows(DeleteReservationException.class, () -> reservationController.deleteReservation(request));
    }

    @Test
    void editReservation() {
    }

    @Test
    void getReservationsByUserId() {
    }

    @Test
    void getReservationsByHotelsId() {
    }

    @Test
    void getReservationsByCorporationId() {
    }

    @Test
    void changeReservationStatus() {
    }
}