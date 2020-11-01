package pl.booking.bookmyroom.reservation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.booking.bookmyroom.hotelservice.exceptions.FindHotelException;
import pl.booking.bookmyroom.hotelservice.service.HotelService;
import pl.booking.bookmyroom.reservation.model.*;
import pl.booking.bookmyroom.reservation.repository.ReservationRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class ReservationService {
    private final ReservationRepository repository;
    private final HotelService hotelService;

    @Autowired
    public ReservationService(ReservationRepository repository, HotelService hotelService) {
        this.repository = repository;
        this.hotelService = hotelService;
    }

    public boolean makeReservation(MakeReservationRequest request) {
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
            return true;
        } else {
            return false;
        }
    }

    public boolean hotelHasFreeRooms(Date startDate, Date endDate, Integer roomId){
        long numOfReservations = repository.findAllByRoomId(roomId).stream()
                .filter(r -> r.isCollidingWith(startDate, endDate))
                .count();
        long numOfRoomsInHotel = (long) hotelService.getNumberOfRoomsByRoomTypeId(roomId);
        return numOfReservations <= numOfRoomsInHotel;
    }

    public boolean deleteReservation(Integer reservationId) {
        Optional<Reservation> reservation = repository.findById(reservationId);
        if(reservation.isPresent()){
            repository.delete(reservation.get());
            return true;
        } else {
            return false;
        }
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

    public boolean editReservation(EditReservationRequest request) {
        Optional<Reservation> reservation = repository.findById(request.getReservationId());
        if (reservation.isPresent()){
            if(hotelHasFreeRooms(request.getStartDate(), request.getEndDate(), request.getIdRoom())) {
                Reservation r = reservation.get();
                r.setReservationStart(request.getStartDate());
                r.setReservationEnd(request.getEndDate());
                r.setHotelsId(request.getIdHotel());
                r.setRoomTypeId(request.getIdRoom());
                repository.save(r);
                return true;
            }
        }
        return false;
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
