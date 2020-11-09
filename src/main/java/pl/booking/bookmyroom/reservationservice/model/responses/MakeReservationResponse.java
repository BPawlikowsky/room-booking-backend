package pl.booking.bookmyroom.reservationservice.model.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.booking.bookmyroom.reservationservice.model.requests.MakeReservationRequest;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@AllArgsConstructor
public class MakeReservationResponse {

    private Date startDate;

    private Date endDate;

    private Integer idUser;

    private Integer idHotel;

    private Integer idRoom;

    private String status;

    public MakeReservationResponse(MakeReservationRequest request, String status) {
        startDate = request.getStartDate();
        endDate = request.getEndDate();
        idUser = request.getIdUser();
        idHotel = request.getIdHotel();
        idRoom = request.getIdRoom();
        this.status = status;
    }
}
