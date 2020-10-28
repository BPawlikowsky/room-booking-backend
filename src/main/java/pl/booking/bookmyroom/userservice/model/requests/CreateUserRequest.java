package pl.booking.bookmyroom.userservice.model.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class CreateUserRequest {
    @NotNull
    @Email
    private final String username;
    @NotNull
    private final String password;
    @NotNull
    private final String passwordVerification;
}
