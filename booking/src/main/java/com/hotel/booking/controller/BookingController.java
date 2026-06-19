package com.hotel.booking.controller;

import com.hotel.booking.dto.*;
import com.hotel.booking.model.Booking;
import com.hotel.booking.model.Enums.Status;
import com.hotel.booking.service.BookingService;
import com.hotel.booking.service.RoomService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    //Dependancy Injection
    @Autowired
    private BookingService bookingservice;

    @Autowired
    private RoomService roomService;


    @PostMapping
    public BookingResponse createBooking(@RequestBody BookingRequest bookingRequest) {
        return bookingservice.saveBooking(bookingRequest);
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
    public List<BookingResponse> getAllBookings() {
        return bookingservice.getAllBookings();
    }

    //@PreAuthorize("@bookingSecurity.IsBookingOwner(#user_id)")
    @GetMapping("/{id}")
    public List<BookingResponse> getUserBookings(@PathVariable Long id) {
        return bookingservice.getUserBookings(id);
    }

    //Can only be accessed by the booking owner or admins
    @PreAuthorize("@bookingSecurity.IsBookingOwner(#id) or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public BookingResponse updateBookingStatus(@PathVariable Long id, @RequestBody UpdateBookingRequest updateBookingRequest) {
        return bookingservice.updateBookingStatus(id, updateBookingRequest.status());
    }

    // Returns the overlapping bookings to frontend
    @GetMapping("/check-availability")
    public RoomAvailabilityResponse checkRoomAvailability(
            @RequestParam Long roomId,
            @RequestParam LocalDate checkIn,
            @RequestParam LocalDate checkOut
    ) {

        List<Booking> bookings =
                bookingservice.getOverlappingBookings(
                        roomId,
                        checkIn,
                        checkOut
                );

        boolean available =
                bookings.isEmpty();

        List<ConflictPeriod> conflicts =
                bookings.stream()
                        .map(b -> new ConflictPeriod(
                                b.getCheckIn(),
                                b.getCheckOut()
                        ))
                        .toList();

        return new RoomAvailabilityResponse(
                roomId,
                checkIn,
                checkOut,
                available,
                conflicts
        );
    }

}