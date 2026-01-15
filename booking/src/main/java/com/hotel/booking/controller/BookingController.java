package com.hotel.booking.controller;

import com.hotel.booking.dto.BookingResponse;
import com.hotel.booking.model.Booking;
import com.hotel.booking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
    /*
    Spring generates a proxy around the method
    It uses the Security Context Authentication object to determine
    if the user is authorized to execute this method or not
    Security Context Auth object is set through the filter chain
    this is executed after the filter chain is fully executed.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<BookingResponse> getAllBookings(){
        return bookingservice.getAllBookings();
    }

    //Can only be accessed by the booking owner
    @PreAuthorize("@bookingSecurity.IsBookingOwner(#id) or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public BookingResponse updateBooking(@PathVariable Long id , @RequestBody Booking newbooking){
        return bookingservice.updateBooking(id,newbooking);
    }
}
