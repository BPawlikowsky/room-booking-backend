package pl.booking.bookmyroom.hotelservice.model.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeleteHotelResponse {

    private Integer id;
    private String status;
}
