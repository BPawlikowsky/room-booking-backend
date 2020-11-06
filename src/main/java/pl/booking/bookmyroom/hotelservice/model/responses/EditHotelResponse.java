package pl.booking.bookmyroom.hotelservice.model.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.booking.bookmyroom.hotelservice.model.requests.EditHotelRequest;

@Getter
@AllArgsConstructor
public class EditHotelResponse {
    private final String phoneNumber;
    private final Integer standard;
    private final String status;

    public EditHotelResponse(EditHotelRequest request, String status) {
        this.phoneNumber = request.getPhoneNumber();
        this.standard = request.getStandard();
        this.status = status;
    }
}
