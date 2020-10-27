package pl.booking.bookmyroom.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.booking.bookmyroom.security.model.Authority;

import java.util.List;

@Repository
public interface AuthRepository extends JpaRepository<Authority, Long> {
    List<Authority> findByEmail(String email);
}
