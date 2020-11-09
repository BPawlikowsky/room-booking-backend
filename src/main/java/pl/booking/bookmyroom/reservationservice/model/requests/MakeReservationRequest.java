package pl.booking.bookmyroom.reservationservice.model.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MakeReservationRequest {
    @NotNull
    private Date startDate;
    @NotNull
    private Date endDate;
    @NotNull
    private Integer idUser;
    @NotNull
    private Integer idHotel;
    @NotNull
    private Integer idRoom;
}
