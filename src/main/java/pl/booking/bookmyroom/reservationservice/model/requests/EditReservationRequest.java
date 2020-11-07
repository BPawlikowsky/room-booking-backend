package pl.booking.bookmyroom.reservationservice.model.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@NoArgsConstructor
public class EditReservationRequest {
    @NotNull
    private Integer reservationId;
    @NotNull
    private Date startDate;
    @NotNull
    private Date endDate;
    @NotNull
    private Integer idHotel;
    @NotNull
    private Integer idRoom;
}
