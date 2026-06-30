package com.hotel.booking.controller;

import com.hotel.booking.model.Booking;
import com.hotel.booking.model.Enums.Status;
import com.hotel.booking.model.Room;
import com.hotel.booking.model.User;
import com.hotel.booking.repository.BookingRepository;
import com.hotel.booking.repository.PaymentRepository;
import com.hotel.booking.repository.RoomRepository;
import com.hotel.booking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
@Profile({"dev" , "staging"})
public class TestController {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;

    @DeleteMapping("/cleanup")
    public void cleanupE2EData() {
        //Cascading type is set as remove
        //when user deleted, all associated records will be deleted as well
        userRepository.findByEmail("e2e@test.com")
                .ifPresent(user ->
                        userRepository.delete(user));


    }

    //Used for tests retries to mitigate the same booking is already booked when test retry
    @DeleteMapping("/cleanup-bookings")
    public void cleanupBookings(){
        paymentRepository.deleteE2EPayments();
        bookingRepository.deleteE2EBookings();
    }

    @GetMapping("/addpastbooking")
    public void addPastBooking() {
        User user = userRepository.findByEmail("e2e@test.com")
                .orElseThrow();

        //Take the first room
        Room room = roomRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow();
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setRoom(room);
        booking.setStatus(Status.CONFIRMED);
        booking.setCheckIn(LocalDate.of(2025, 1, 1));
        booking.setCheckOut(LocalDate.of(2025, 1, 5));
        booking.setCreatedAt(LocalDate.of(2025, 1, 5));
        booking.setTotalPayment(room.getPrice() * 4);
        bookingRepository.save(booking);
    }



}