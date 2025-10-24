package com.hotel.booking.controller;

import com.hotel.booking.model.Booking;
import com.hotel.booking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    //Dependancy Injection
    @Autowired
    private BookingService bookingservice;

    @PostMapping
    public Booking createBooking(@RequestBody Booking booking){
        return bookingservice.saveBooking(booking);
    }
    //for testing
    @GetMapping
    public List<Booking> getAllBookings(){
        return bookingservice.getAllBookings();
    }

    @PutMapping("/{id}")
    public Booking updateBooking(@PathVariable Long id , @RequestBody Booking newbooking){
        return bookingservice.updateBooking(id,newbooking);
    }
}
