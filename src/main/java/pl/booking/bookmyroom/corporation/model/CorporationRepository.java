package pl.booking.bookmyroom.corporation.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Email;
import java.util.List;

@Repository
public interface CorporationRepository extends JpaRepository<Corporation, Integer> {

    @Query(value = "SELECT c FROM Corporation c WHERE c.email=:email")
    public List<Corporation> findCorporationByEmail(@Email String email);

    List<Corporation> findByEmail(String userName);
}
