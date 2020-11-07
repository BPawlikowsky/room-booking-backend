package pl.booking.bookmyroom.reservationservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date reservationStart, reservationEnd;
    private Integer hotelsId, roomTypeId, userId;
    private Float fullReservationPrice;
    private ReservationStatus status;

    public boolean isCollidingWith(Date otherReservationStart, Date otherReservationEnd){
        if (this.reservationStart.before(otherReservationStart) && this.reservationEnd.after(otherReservationStart)){
            return true;
        }
        if (this.reservationStart.before(otherReservationEnd) && this.reservationEnd.after(otherReservationEnd)){
            return true;
        }
        if (this.reservationStart.after(otherReservationStart) && this.reservationEnd.before(otherReservationEnd)){
            return true;
        }
        if (this.reservationStart.equals(otherReservationStart) || this.reservationEnd.equals(otherReservationEnd)){
            return true;
        }
        return false;
    }
}
