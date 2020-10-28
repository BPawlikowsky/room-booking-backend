package pl.booking.bookmyroom.corporationservice.model.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateCorporationRequest {

    @NotNull
    private String name;

    @NotNull
    @Email
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String passwordVerification;


}
