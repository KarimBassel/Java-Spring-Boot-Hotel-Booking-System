package com.hotel.booking.repository;
import com.hotel.booking.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    //COALESCE returns the first non-null value
    @Query("SELECT COALESCE(AVG(r.review), 0)FROM Review r WHERE r.hotel.id = :hotelId")
    Double findAverageRating(@Param("hotelId") Long hotelId);

}
