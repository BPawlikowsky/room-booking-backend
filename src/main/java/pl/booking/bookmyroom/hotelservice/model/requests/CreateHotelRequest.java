package pl.booking.bookmyroom.hotelservice.model.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateHotelRequest {

    @NotNull
    private String city;

    @NotNull
    private String name;

    @NotNull
    private String street;

    @NotNull
    private String streetNumber;

    @NotNull
    private String phoneNumber;

    @NotNull
    @Max(5)
    private Integer standard;

    @NotNull
    private Integer corporationId;

    @NotNull
    @Valid
    private AddRoomsToHotelRequest[] roomsToHotelRequests;
}
