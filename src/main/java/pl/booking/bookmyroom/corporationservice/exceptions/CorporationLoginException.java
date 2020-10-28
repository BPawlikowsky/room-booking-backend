package pl.booking.bookmyroom.corporationservice.exceptions;

import javax.validation.constraints.NotNull;

public class CorporationLoginException extends Exception {
    public CorporationLoginException(@NotNull String message) {
        super(message);
    }
}
