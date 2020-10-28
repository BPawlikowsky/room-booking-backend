package pl.booking.bookmyroom.userservice.exceptions;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class LoginUserException extends Exception {
    public LoginUserException(String message) {
        super(message);
    }
}
