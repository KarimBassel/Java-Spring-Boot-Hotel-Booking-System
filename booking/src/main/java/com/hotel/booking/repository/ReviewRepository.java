package com.hotel.booking.repository;

import com.hotel.booking.model.Review;
import com.hotel.booking.model.Hotel;
import com.hotel.booking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review , Long> {
    List<Review> findByHotel(Hotel hotel);
    List<Review> findByUser(User user);
}
