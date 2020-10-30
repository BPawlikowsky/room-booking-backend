package pl.booking.bookmyroom.hotelservice.model.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.booking.bookmyroom.hotelservice.model.Hotel;
import pl.booking.bookmyroom.hotelservice.model.RoomType;


import javax.validation.constraints.NotNull;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetHotelResponse {
    @NotNull
    private Hotel hotel;

    @NotNull
    private List<RoomType> hotelRoomTypes;
}
