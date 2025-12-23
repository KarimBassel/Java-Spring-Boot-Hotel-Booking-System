package com.hotel.booking.service;

import com.hotel.booking.model.Hotel;
import com.hotel.booking.model.Review;
import com.hotel.booking.model.Room;
import com.hotel.booking.repository.HotelRepository;
import com.hotel.booking.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hotel.booking.dto.HotelResponse;
import java.util.List;
import java.util.Optional;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    // WRITE operations → Entity is OK
    public Hotel addHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    public Optional<Hotel> updateHotel(Long hotelId, Hotel updatedHotel) {
        Optional<Hotel> optionalHotel = hotelRepository.findById(hotelId);
        if (optionalHotel.isPresent()) {
            Hotel hotel = optionalHotel.get();
            hotel.setName(updatedHotel.getName());
            hotel.setLocation(updatedHotel.getLocation());
            hotel.setDescription(updatedHotel.getDescription());
            hotelRepository.save(hotel);
        }
        return optionalHotel;
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
    private HotelResponse mapToHotelResponse(Hotel hotel) {
        return new HotelResponse(
                hotel.getId(),
                hotel.getName(),
                hotel.getLocation(),
                hotel.getDescription(),
                getAverageRating(hotel.getId()),
                hotel.getRooms()   // keep as-is if Room DTO not introduced yet
        );
    }
}

