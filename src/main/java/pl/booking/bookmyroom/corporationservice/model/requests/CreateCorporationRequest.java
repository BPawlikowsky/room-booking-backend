package pl.booking.bookmyroom.corporationservice.model.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Data
public class CreateCorporationRequest {

    @NotNull
    private String name;

    @NotNull
    private String password;

    @NotNull
    private String passwordVerification;

    @NotNull
    @Email
    private String username;
}
