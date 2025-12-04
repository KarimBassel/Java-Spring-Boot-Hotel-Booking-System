package com.hotel.booking.service;

import com.hotel.booking.model.Hotel;
import com.hotel.booking.model.Review;
import com.hotel.booking.model.Room;
import com.hotel.booking.repository.HotelRepository;
import com.hotel.booking.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private ReviewRepository reviewRepository;

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


    public Hotel getHotelById(Long hotelId) {
        return hotelRepository.findById(hotelId).orElse(null);
    }


    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    public double getAverageRating(Long hotelId) {
        Optional<Hotel> optionalHotel = hotelRepository.findById(hotelId);
        if (optionalHotel.isEmpty()) return 0.0;

        List<Review> reviews = reviewRepository.findByHotel(optionalHotel.get());
        return reviews.stream()
                .mapToDouble(Review::getReview)
                .average()
                .orElse(0.0);
    }
}
