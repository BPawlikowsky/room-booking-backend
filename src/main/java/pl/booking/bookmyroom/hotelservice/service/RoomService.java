package pl.booking.bookmyroom.hotelservice.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import pl.booking.bookmyroom.hotelservice.model.requests.AddRoomsToHotelRequest;
import pl.booking.bookmyroom.hotelservice.model.RoomStandard;
import pl.booking.bookmyroom.hotelservice.model.RoomType;
import pl.booking.bookmyroom.hotelservice.repository.RoomRepository;
import pl.booking.bookmyroom.reservationservice.service.ReservationService;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
class RoomService {

    private final RoomRepository roomRepository;
    private final ReservationService reservationService;

    public RoomService(RoomRepository roomRepository, @Lazy ReservationService reservationService) {
        this.roomRepository = roomRepository;
        this.reservationService = reservationService;
    }

    void addRoomsToHotel(AddRoomsToHotelRequest request, Integer hotelsId){
        RoomType roomType = new RoomType();
        roomType.setHotelId(hotelsId);
        roomType.setNumberOfBeds(request.getNumOfBeds());
        roomType.setNumberOfRooms(request.getNumOfRooms());
        roomType.setPrice(request.getPrice());
        roomType.setStandard(request.getStandard());
        roomRepository.save(roomType);
    }

    List<RoomType> getAllRoomTypes(){
        return roomRepository.findAll();
    }

    List<RoomType> getRoomTypesByHotelsId(Integer hotelsId){
        return roomRepository.findByHotelsId(hotelsId);
    }

    RoomType getRoomTypeById(Integer id) {
        return roomRepository.findById(id).get();
    }

    Integer getNumberOfRoomsById(Integer id) {
        return roomRepository.findById(id).get().getNumberOfRooms();
    }

    List<RoomType> anyRoomsMatchQuery(Integer hotelsId,
                               Optional<Integer> numOfBeds,
                               Optional<RoomStandard> standard,
                               Optional<Float> priceMin,
                               Optional<Float> priceMax,
                               Optional<Date> start,
                               Optional<Date> end){
        List<RoomType> hotelRooms = hotelsId != null ? roomRepository.findByHotelsId(hotelsId) : roomRepository.findAll();
        hotelRooms = numOfBeds.isPresent() ? getRoomsMatchingNumberOfBeds(hotelRooms, numOfBeds.get()) : hotelRooms;
        hotelRooms = standard.isPresent() ? getRoomsMatchingStandard(hotelRooms, standard.get()) : hotelRooms;
        hotelRooms = priceMin.isPresent() && priceMax.isPresent() ? getRoomsMatchingPriceRange(hotelRooms, priceMin.get(), priceMax.get()) : hotelRooms;
        hotelRooms = start.isPresent() && end.isPresent() ? getRoomsMatchingDateRange(hotelRooms, start.get(), end.get()) : hotelRooms;
        return hotelRooms;
    }

    private List<RoomType> getRoomsMatchingNumberOfBeds(List<RoomType> hotelRooms, Integer numberOfBeds){
        return hotelRooms.stream()
                .filter(r -> r.getNumberOfBeds().equals(numberOfBeds))
                .collect(Collectors.toList());
    }

    private   List<RoomType> getRoomsMatchingStandard(List<RoomType> hotelRooms, RoomStandard standard){
        return hotelRooms.stream()
                .filter(r -> r.getStandard().equals(standard))
                .collect(Collectors.toList());
    }

    private List<RoomType> getRoomsMatchingPriceRange(List<RoomType> hotelRooms, Float priceMin, Float priceMax){
        return hotelRooms.stream()
                .filter(r -> r.getPrice() >= priceMin &&
                        r.getPrice() <= priceMax)
                .collect(Collectors.toList());
    }

    private List<RoomType> getRoomsMatchingDateRange(List<RoomType> hotelRooms, Date start, Date end){
        return hotelRooms.stream()
                .filter(r -> reservationService.hotelHasFreeRooms(start, end, r.getId()))
                .collect(Collectors.toList());
    }
}