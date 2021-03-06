package pl.booking.bookmyroom.reservation.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@NoArgsConstructor
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
