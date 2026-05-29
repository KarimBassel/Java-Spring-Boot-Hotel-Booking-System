package com.hotel.booking.repository;

import com.hotel.booking.model.Hotel;
import com.hotel.booking.model.Review;
import com.hotel.booking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByHotel(Hotel hotel);

    List<Review> findByUser(User user);

    boolean existsByUserAndHotel(User user, Hotel hotel);

    List<Review> findByHotelOrderByIdDesc(Hotel hotel);

    long countByHotel(Hotel hotel);
    @Query("""
            SELECT r
            FROM Review r
            WHERE r.user.id = :userID
            AND r.hotel.id = :hotelID
            """)
    Review getUserHotelReview(@Param("userID") Long userID,@Param("hotelID") Long hotelID);
}