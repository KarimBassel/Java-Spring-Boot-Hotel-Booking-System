package com.hotel.booking.controller;

import com.hotel.booking.model.Booking;
import com.hotel.booking.model.Enums.Status;
import com.hotel.booking.model.Review;
import com.hotel.booking.model.Room;
import com.hotel.booking.model.User;
import com.hotel.booking.repository.BookingRepository;
import com.hotel.booking.repository.RoomRepository;
import com.hotel.booking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/test")
@Profile({"dev" , "staging"})
public class TestController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @DeleteMapping("/cleanup")
    public void cleanupE2EData() {
        //Cascading type is set as remove
        //when user deleted, all associated records will be deleted as well
        userRepository.findByEmail("e2e@test.com")
                .ifPresent(user ->
                        userRepository.delete(user));


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