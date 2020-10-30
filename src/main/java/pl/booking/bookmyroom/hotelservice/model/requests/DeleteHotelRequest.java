package pl.booking.bookmyroom.hotelservice.model.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteHotelRequest {

    @NotNull
    private Integer id;
}
