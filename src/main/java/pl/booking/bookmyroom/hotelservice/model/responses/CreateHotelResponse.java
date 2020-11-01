package pl.booking.bookmyroom.hotelservice.model.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.booking.bookmyroom.hotelservice.model.requests.CreateHotelRequest;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateHotelResponse {
    @NotNull
    private String city;

    @NotNull
    private String name;

    @NotNull
    private Integer corporationId;

    @NotNull
    private String status;

    public CreateHotelResponse(CreateHotelRequest request, String status) {
        this.city = request.getCity();
        this.name = request.getName();
        this.corporationId = request.getCorporationId();
        this.status = status;
    }
}
