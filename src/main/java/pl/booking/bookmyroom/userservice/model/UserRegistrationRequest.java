package pl.booking.bookmyroom.userservice.model;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
public class UserRegistrationRequest {
    @NotNull
    @Email
    private final String email;
    @NotNull
    private final String password;
    @NotNull
    private final String passwordVerify;

    public UserRegistrationRequest(@NotNull @Email String email, @NotNull String password, @NotNull String passwordVerify) {
        this.email = email;
        this.password = password;
        this.passwordVerify = passwordVerify;
    }
}
