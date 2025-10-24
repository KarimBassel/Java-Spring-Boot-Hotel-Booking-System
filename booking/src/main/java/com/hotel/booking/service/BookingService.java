package com.hotel.booking.service;

import com.hotel.booking.model.Booking;
import com.hotel.booking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingrepository;

    //for testing
    public List<Booking> getAllBookings(){
        return bookingrepository.findAll();
    }
    public Booking saveBooking(Booking booking){
        return bookingrepository.save(booking);
    }
    public List<Booking> getUserBookings(Long uid){
        return bookingrepository.findByUserId(uid);
    }
    public Booking getBookingbyID(Long bid){
        return bookingrepository.getReferenceById(bid);
    }
    public Booking updateBooking(Long id,Booking newbooking){
        //get current booking
        Booking b = bookingrepository.getReferenceById(id);
        if(b != null){
            b.setRoom(newbooking.getRoom());
            b.setCheckIn(newbooking.getCheckIn());
            b.setCheckOut(newbooking.getCheckOut());
            b.setStatus(newbooking.getStatus());
            b.setTotalPayment(newbooking.getTotalPayment());
            return bookingrepository.save(b);
        }
        return null;

    }
}
