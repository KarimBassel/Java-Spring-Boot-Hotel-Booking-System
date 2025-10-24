package com.hotel.booking.repository;
import com.hotel.booking.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking , Long> {
    @Query("SELECT b from Booking b where b.user.id = :userId")
    List<Booking> findByUserId(@Param("userId") Long userId);

}
