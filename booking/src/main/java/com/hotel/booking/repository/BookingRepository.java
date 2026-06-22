package com.hotel.booking.repository;
import com.hotel.booking.model.Booking;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking , Long> {
    @Query("SELECT b from Booking b where b.user.id = :userId")
    List<Booking> findByUserId(@Param("userId") Long userId);

    @Query("""
        SELECT COUNT(b) > 0
        FROM Booking b
        WHERE b.room.id = :roomId
        AND b.status <> 'CANCELLED'
        AND b.checkIn < :checkOut
        AND b.checkOut > :checkIn
    """)
    boolean existsOverlappingBooking(
            @Param("roomId") Long roomId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut
    );


    @Query("""
       SELECT b
       FROM Booking b
       WHERE b.room.id = :roomId
       AND b.status <> 'CANCELLED'
       AND b.checkIn < :checkOut
       AND b.checkOut > :checkIn
       """)
    List<Booking> findOverlappingBookings(
            @Param("roomId") Long roomId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut
    );

    //Modifies data so must annotate with @Modifying
    @Modifying
    @Transactional
    @Query("""
    DELETE FROM Booking b
    WHERE b.user.email = 'e2e@test.com'
""")
    void deleteE2EBookings();




}
