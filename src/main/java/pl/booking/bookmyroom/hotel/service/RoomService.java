package pl.booking.bookmyroom.hotel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.booking.bookmyroom.hotel.model.AddRoomsToHotelRequest;
import pl.booking.bookmyroom.hotel.model.RoomStandard;
import pl.booking.bookmyroom.hotel.model.RoomType;
import pl.booking.bookmyroom.hotel.repository.RoomRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
class RoomService {

    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
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

    boolean anyRoomsMatchQuery(Integer hotelsId,
                                      Optional<Integer> numOfBeds,
                                      Optional<RoomStandard> standard,
                                      Optional<Float> priceMin,
                                      Optional<Float> priceMax){
        List<RoomType> hotelRooms = roomRepository.findByHotelsId(hotelsId);
        hotelRooms = numOfBeds.isPresent() ? getRoomsMatchingNumberOfBeds(hotelRooms, numOfBeds.get()) : hotelRooms;
        hotelRooms = standard.isPresent() ? getRoomsMatchingStandard(hotelRooms, standard.get()) : hotelRooms;
        hotelRooms = priceMin.isPresent() && priceMax.isPresent() ? getRoomsMatchingPriceRange(hotelRooms, priceMin.get(), priceMax.get()) : hotelRooms;
        return !hotelRooms.isEmpty();
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
}