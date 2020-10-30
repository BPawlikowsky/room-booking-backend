package pl.booking.bookmyroom.hotelservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.booking.bookmyroom.hotelservice.model.RoomType;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<RoomType, Integer> {

    @Query(value = "SELECT rt FROM RoomType rt WHERE rt.hotelId=:hotelId")
    List<RoomType> findByHotelsId(Integer hotelId);
}
