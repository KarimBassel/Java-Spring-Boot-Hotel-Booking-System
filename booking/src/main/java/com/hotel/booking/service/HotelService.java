package com.hotel.booking.service;

import com.hotel.booking.dto.CreateHotelRequest;
import com.hotel.booking.dto.RoomResponse;
import com.hotel.booking.dto.UpdateHotelRequest;
import com.hotel.booking.model.Hotel;
import com.hotel.booking.model.Review;
import com.hotel.booking.model.Room;
import com.hotel.booking.repository.HotelRepository;
import com.hotel.booking.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hotel.booking.dto.HotelResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    // WRITE operations → Entity is OK
    public HotelResponse addHotel(CreateHotelRequest createHotelRequest) {
        Hotel newhotel = new Hotel();
        newhotel.setName(createHotelRequest.name());
        newhotel.setDescription(createHotelRequest.description());
        newhotel.setLocation(createHotelRequest.location());
        newhotel.setImageUrl(createHotelRequest.image_url());
        hotelRepository.save(newhotel);
        return mapToHotelResponse(newhotel);
    }

    public HotelResponse updateHotel(Long HotelID, UpdateHotelRequest updateHotelRequest) throws Exception {
        Hotel hotel = hotelRepository.findById(HotelID)
                .orElseThrow(() ->
                        new Exception(
                                "Hotel with id " + HotelID + " not found"));

        hotel.setName(updateHotelRequest.name());
        hotel.setLocation(updateHotelRequest.location());
        hotel.setDescription(updateHotelRequest.description());
        hotel.setImageUrl(updateHotelRequest.image_url());

        hotelRepository.save(hotel);

        return mapToHotelResponse(hotel);
    }

    public boolean deleteHotel(Long hotelId) {
        if (hotelRepository.existsById(hotelId)) {
            hotelRepository.deleteById(hotelId);
            return true;
        }
        return false;
    }

    // READ operations → DTO
    public HotelResponse getHotelById(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId).orElse(null);
        if (hotel == null) return null;

        return mapToHotelResponse(hotel);
    }

    public List<HotelResponse> getAllHotels() {
        return hotelRepository.findAll()
                .stream()
                .map(this::mapToHotelResponse)
                .toList();
    }

    // SAME LOGIC, unchanged
    public double getAverageRating(Long hotelId) {
        Optional<Hotel> optionalHotel = hotelRepository.findById(hotelId);
        if (optionalHotel.isEmpty()) return 0.0;

        List<Review> reviews = reviewRepository.findByHotel(optionalHotel.get());
        return reviews.stream()
                .mapToDouble(Review::getReview)
                .average()
                .orElse(0.0);
    }
    // DTO mapping (private helper)
    //If Needed --> Mapper Classes should be created, but for now it is manageable
    private HotelResponse mapToHotelResponse(Hotel hotel) {
        List<Room> rooms = hotel.getRooms();
        List<RoomResponse> roomresponses= new ArrayList<>();
        //Creating a new hotel has no rooms yet
        if(!rooms.isEmpty()){
            for(Room room : rooms){
                roomresponses.add(maptoRoomResponse(room));
            }
        }
        //This step is better than constructing the hotel object from the ID and getting the average
        //it is better to query the database directly through the repository
        //and receive instant response
        double rating = hotelRepository.findAverageRating(hotel.getId());
        return new HotelResponse(
                hotel.getId(),
                hotel.getName(),
                hotel.getLocation(),
                hotel.getDescription(),
                rating,
                roomresponses,
                hotel.getImageUrl()
        );
    }
    private RoomResponse maptoRoomResponse(Room room){
        return new RoomResponse(
                room.getId(),
                room.getRoomNumber(),
                room.getRoomType(),
                room.getPrice(),
                room.isAvailability(),
                room.getImageUrl(),
                room.getHotel().getId(),
                room.getHotel().getName()
                );
    }
}

