package pl.booking.bookmyroom.reservationservice.model.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.booking.bookmyroom.reservationservice.model.ReservationStatus;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class ChangeStatusRequest {
    @NotNull
    private Integer reservationId;

    @NotNull
    private ReservationStatus status;
}
