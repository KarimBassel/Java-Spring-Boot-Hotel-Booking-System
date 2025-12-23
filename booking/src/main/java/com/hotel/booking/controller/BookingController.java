package com.hotel.booking.controller;

import com.hotel.booking.dto.BookingResponse;
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
    public BookingResponse createBooking(@RequestBody Booking booking){
        return bookingservice.saveBooking(booking);
    }
    //for testing
    @GetMapping
    public List<BookingResponse> getAllBookings(){
        return bookingservice.getAllBookings();
    }

    @PutMapping("/{id}")
    public BookingResponse updateBooking(@PathVariable Long id , @RequestBody Booking newbooking){
        return bookingservice.updateBooking(id,newbooking);
    }
}
