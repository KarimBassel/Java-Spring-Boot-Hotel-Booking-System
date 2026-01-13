package com.hotel.booking.security;

import com.hotel.booking.dto.UserResponse;
import com.hotel.booking.model.Booking;
import com.hotel.booking.repository.BookingRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Principal;
/*
This is intended to be defined & user in Spring Security expression language
 */
@Component("bookingSecurity")
public class BookingSecurity {
    private final BookingRepository bookingRepository;

    public BookingSecurity(BookingRepository bookingRepository){
        this.bookingRepository = bookingRepository;
    }

    public boolean IsBookingOwner(Long bookingID){
        //get Authentication object
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        //get Principal which is set in the filter chain
        //Principal is the UserResponse Object
        Object principal = auth.getPrincipal();

        if(!(principal instanceof UserResponse)){
            return false;
        }
        UserResponse AuthenticatedUser = (UserResponse)principal;
        //if Principal object is UserResponse get the user email and compare it with
        //email of booking object
        Booking booking = bookingRepository.findById(bookingID).orElse(null);

        if(booking == null)return false;

        return booking.getUser().getEmail().equals(AuthenticatedUser.email());

    }
}
