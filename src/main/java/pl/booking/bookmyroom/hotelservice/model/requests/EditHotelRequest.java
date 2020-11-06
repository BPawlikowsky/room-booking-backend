package pl.booking.bookmyroom.hotelservice.model.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditHotelRequest {

    private String phoneNumber;

    @Max(5)
    private Integer standard;
}
