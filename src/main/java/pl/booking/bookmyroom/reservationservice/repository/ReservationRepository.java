package pl.booking.bookmyroom.reservationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.booking.bookmyroom.reservationservice.model.Reservation;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    @Query(value = "SELECT r FROM Reservation r WHERE r.roomTypeId=:roomTypeId")
    List<Reservation> findAllByRoomId(Integer roomTypeId);

    @Query(value = "SELECT r FROM Reservation r WHERE r.userId=:userId")
    List<Reservation> findAllByUserId(Integer userId);

    @Query(value = "SELECT r FROM Reservation r WHERE r.hotelsId=:hotelsId")
    List<Reservation> findAllByHotelsId(Integer hotelsId);
}
