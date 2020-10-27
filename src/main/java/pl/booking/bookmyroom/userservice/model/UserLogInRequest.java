package pl.booking.bookmyroom.userservice.model;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
public class UserLogInRequest {
    @NotNull
    @Email
    private final String email;
    @NotNull
    private final String password;

    public UserLogInRequest(@NotNull @Email String email, @NotNull String password) {
        this.email = email;
        this.password = password;
    }
}
