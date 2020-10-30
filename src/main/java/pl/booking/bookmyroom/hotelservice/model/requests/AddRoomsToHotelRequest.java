package pl.booking.bookmyroom.hotelservice.model.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.booking.bookmyroom.hotelservice.model.RoomStandard;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class AddRoomsToHotelRequest {
    @NotNull
    private Float price;

    @NotNull
    private Integer numOfBeds;

    @NotNull
    private RoomStandard standard;

    @NotNull
    private Integer numOfRooms;
}
