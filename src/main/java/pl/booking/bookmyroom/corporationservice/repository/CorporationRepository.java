package pl.booking.bookmyroom.corporationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.booking.bookmyroom.corporationservice.model.Corporation;

import javax.validation.constraints.Email;
import java.util.Optional;

@Repository
public interface CorporationRepository extends JpaRepository<Corporation, Integer> {

    public Optional<Corporation> findCorporationByUsername(@Email String username);
    Optional<Corporation> findByUsername(String userName);
}
