package pl.booking.bookmyroom.corporationservice.model.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class LoginCorporationRequest {

    @NotNull
    private String username;

    @NotNull
    private String password;
}
