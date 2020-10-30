package pl.booking.bookmyroom.hotelservice.exceptions;

public class CreateHotelException extends Throwable {
    public CreateHotelException(String status) {
        super(status);
    }
}
