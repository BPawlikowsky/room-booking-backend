package pl.booking.bookmyroom.userservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.Email;

@Entity
@Table(name = "user")
@NoArgsConstructor
@Getter @Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Email
    @Column(unique = true)
    private String email;
    private String password;
    private boolean active;
    private String roles;
}
