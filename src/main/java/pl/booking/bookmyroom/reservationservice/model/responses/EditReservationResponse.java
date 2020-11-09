package pl.booking.bookmyroom.reservationservice.model.responses;

import lombok.Getter;
import pl.booking.bookmyroom.reservationservice.model.requests.EditReservationRequest;

import java.util.Date;

@Getter
public class EditReservationResponse {

    private Integer reservationId;

    private Date startDate;

    private Date endDate;

    private Integer idHotel;

    private Integer idRoom;

    private String status;

    public EditReservationResponse(EditReservationRequest request, String status) {
        reservationId = request.getReservationId();
        startDate = request.getStartDate();
        endDate = request.getEndDate();
        idHotel = request.getIdHotel();
        idRoom = request.getIdRoom();
        this.status = status;
    }
}
