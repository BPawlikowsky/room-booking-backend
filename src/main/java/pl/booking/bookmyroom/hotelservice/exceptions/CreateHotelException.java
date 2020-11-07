package pl.booking.bookmyroom.hotelservice.exceptions;

public class CreateHotelException extends Exception {
    public CreateHotelException(String status) {
        super(status);
    }
}
