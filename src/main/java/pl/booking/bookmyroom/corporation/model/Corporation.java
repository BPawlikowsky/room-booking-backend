package pl.booking.bookmyroom.corporation.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.booking.bookmyroom.userservice.model.User;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.List;


public class Corporation extends User {

    @Setter @Getter
    private String name;
}
