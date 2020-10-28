package pl.booking.bookmyroom.userservice.model.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserResponse {

    private final String username;
    private final String status;
}
