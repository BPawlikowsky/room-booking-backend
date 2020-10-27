package pl.booking.bookmyroom.security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoggedResponse {

    private UserRole type;

    private String name;

}
