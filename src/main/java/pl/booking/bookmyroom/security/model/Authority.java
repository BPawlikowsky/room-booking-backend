package pl.booking.bookmyroom.security.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.booking.bookmyroom.userservice.model.User;

import javax.persistence.*;

@Entity
@Table(name = "authorities")
@NoArgsConstructor
@Getter
@Setter
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String email;
    private String authority;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_authorities_users", referencedColumnName = "email")
    private User user;

}
