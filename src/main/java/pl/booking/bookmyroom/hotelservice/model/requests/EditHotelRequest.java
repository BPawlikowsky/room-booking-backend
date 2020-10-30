package pl.booking.bookmyroom.hotelservice.model.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;

@Data
@NoArgsConstructor
public class EditHotelRequest {

    private String phoneNumber;

    @Max(5)
    private Integer standard;
}
