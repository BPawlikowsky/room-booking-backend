package pl.booking.bookmyroom.reservationservice.model.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeleteReservationResponse {

    private Integer reservationId;

    private String status;
}
