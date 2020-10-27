package pl.booking.bookmyroom.userservice.model.requests;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
public class CreateUserRequest {
    @NotNull
    @Email
    private final String email;
    @NotNull
    private final String password;
    @NotNull
    private final String passwordVerify;

    public CreateUserRequest(@NotNull @Email String email, @NotNull String password, @NotNull String passwordVerify) {
        this.email = email;
        this.password = password;
        this.passwordVerify = passwordVerify;
    }
}
