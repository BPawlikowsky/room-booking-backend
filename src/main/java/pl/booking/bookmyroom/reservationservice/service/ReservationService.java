package pl.booking.bookmyroom.reservationservice.service;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.booking.bookmyroom.hotelservice.exceptions.FindHotelException;
import pl.booking.bookmyroom.hotelservice.service.HotelService;
import pl.booking.bookmyroom.reservationservice.exceptions.DeleteReservationException;
import pl.booking.bookmyroom.reservationservice.exceptions.EditReservationException;
import pl.booking.bookmyroom.reservationservice.exceptions.MakeReservationException;
import pl.booking.bookmyroom.reservationservice.model.*;
import pl.booking.bookmyroom.reservationservice.model.requests.ChangeStatusRequest;
import pl.booking.bookmyroom.reservationservice.model.requests.EditReservationRequest;
import pl.booking.bookmyroom.reservationservice.model.requests.MakeReservationRequest;
import pl.booking.bookmyroom.reservationservice.model.responses.DeleteReservationResponse;
import pl.booking.bookmyroom.reservationservice.model.responses.EditReservationResponse;
import pl.booking.bookmyroom.reservationservice.model.responses.MakeReservationResponse;
import pl.booking.bookmyroom.reservationservice.repository.ReservationRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@NoArgsConstructor
@Service
public class ReservationService {
    private ReservationRepository repository;
    private HotelService hotelService;

    @Autowired
    public ReservationService(ReservationRepository repository, HotelService hotelService) {
        this.repository = repository;
        this.hotelService = hotelService;
    }

    public MakeReservationResponse makeReservation(MakeReservationRequest request) throws MakeReservationException {
        String status;
        if (hotelHasFreeRooms(request.getStartDate(), request.getEndDate(), request.getIdRoom())) {
            Reservation reservation = new Reservation();
            reservation.setReservationStart(request.getStartDate());
            reservation.setReservationEnd(request.getEndDate());
            reservation.setHotelsId(request.getIdHotel());
            reservation.setRoomTypeId(request.getIdRoom());
            reservation.setUserId(request.getIdUser());
            reservation.setFullReservationPrice(hotelService.getRoomPriceByRoomTypeId(request.getIdRoom())*calcNumOfDays(request.getStartDate(), request.getEndDate()));
            reservation.setStatus(ReservationStatus.PENDING);
            repository.save(reservation);
            status = "Reservation successful";
        } else {
            status = "Could not make reservation";
            throw new MakeReservationException(status);
        }
        return new MakeReservationResponse(request, status);
    }

    public boolean hotelHasFreeRooms(Date startDate, Date endDate, Integer roomId){
        long numOfReservations = repository.findAllByRoomId(roomId).stream()
                .filter(r -> r.isCollidingWith(startDate, endDate))
                .count();
        long numOfRoomsInHotel = (long) hotelService.getNumberOfRoomsByRoomTypeId(roomId);
        return numOfReservations <= numOfRoomsInHotel;
    }

    public DeleteReservationResponse deleteReservation(Integer reservationId) throws DeleteReservationException {
        String status;
        Optional<Reservation> reservation = repository.findById(reservationId);
        if(reservation.isPresent()) {
            repository.delete(reservation.get());
            status = "Reservation successfully deleted";
        } else {
            status = "Could not find reservation with id " + reservationId;
            throw new DeleteReservationException(status);
        }
        return new DeleteReservationResponse(reservationId, status);
    }

    public List<Reservation> getUserReservations(Integer userId){
        return repository.findAllByUserId(userId);
    }

    public List<Reservation> getHotelReservation(Integer hotelsId){
        return repository.findAllByHotelsId(hotelsId);
    }

    public List<Reservation> getCorporationReservations(Integer corporationId) throws FindHotelException {
        List<Reservation> reservations = new ArrayList<>();
        hotelService.getHotelsByCorporationId(corporationId)
                .forEach(h -> reservations.addAll(getHotelReservation(h.getId())));
        return reservations;
    }

    public EditReservationResponse editReservation(EditReservationRequest request) throws EditReservationException {
        String status;
        Optional<Reservation> reservation = repository.findById(request.getReservationId());
        if (reservation.isPresent()){
            if(hotelHasFreeRooms(request.getStartDate(), request.getEndDate(), request.getIdRoom())) {
                Reservation r = reservation.get();
                r.setReservationStart(request.getStartDate());
                r.setReservationEnd(request.getEndDate());
                r.setHotelsId(request.getIdHotel());
                r.setRoomTypeId(request.getIdRoom());
                repository.save(r);
                status = "Reservation edited successfully";
            }
            else {
                //todo: After modification of hotelHasFreeRooms add accurate status
                status = "Has no free rooms...";
                throw new EditReservationException(status);
            }
        }
        else {
            status = "Could not find reservation with id " + request.getReservationId();
            throw new EditReservationException(status);
        }

        return new EditReservationResponse(
                request,
                status
        );
    }

    public boolean changeReservationStatus(ChangeStatusRequest request){
        Optional<Reservation> reservation = repository.findById(request.getReservationId());
        if (reservation.isPresent()){
            Reservation r = reservation.get();
            r.setStatus(request.getStatus());
            repository.save(r);
            return true;
        }
        return false;
    }

    private Integer calcNumOfDays(Date startDate, Date endDate){
        long diffInMilis = Math.abs(endDate.getTime() - startDate.getTime());
        long numOfDays = TimeUnit.DAYS.convert(diffInMilis, TimeUnit.MILLISECONDS);
        return (int) numOfDays;
    }
}