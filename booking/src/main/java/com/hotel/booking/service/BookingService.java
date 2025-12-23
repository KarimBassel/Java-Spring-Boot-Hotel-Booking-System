package com.hotel.booking.service;

import com.hotel.booking.dto.BookingResponse;
import com.hotel.booking.model.Booking;
import com.hotel.booking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingrepository;

    //for testing
    public List<BookingResponse> getAllBookings(){
        List<Booking> list=  bookingrepository.findAll();
        List<BookingResponse> responses = new ArrayList<>();
        for(Booking booking : list){
            responses.add(maptoBookingResponse(booking));
        }
        return responses;
    }
    public BookingResponse saveBooking(Booking booking){
        return maptoBookingResponse(bookingrepository.save(booking));
    }
    public List<BookingResponse> getUserBookings(Long uid){
        List<Booking> list = bookingrepository.findByUserId(uid);
        List<BookingResponse> responses = new ArrayList<>();
        for(Booking booking : list){
            responses.add(maptoBookingResponse(booking));
        }
        return responses;
    }
    public BookingResponse getBookingbyID(Long bid){
        return maptoBookingResponse(bookingrepository.getReferenceById(bid));
    }
    public BookingResponse updateBooking(Long id,Booking newbooking){
        //get current booking
        Booking b = bookingrepository.getReferenceById(id);
        if(b != null){
            b.setRoom(newbooking.getRoom());
            b.setCheckIn(newbooking.getCheckIn());
            b.setCheckOut(newbooking.getCheckOut());
            b.setStatus(newbooking.getStatus());
            b.setTotalPayment(newbooking.getTotalPayment());
            return maptoBookingResponse(bookingrepository.save(b));
        }
        return null;

    }
    public BookingResponse maptoBookingResponse(Booking bookingobj){
        return new BookingResponse(
                bookingobj.getId(),
                bookingobj.getRoom().getHotel().getName(),
                bookingobj.getRoom().getRoomNumber(),
                bookingobj.getCheckIn(),
                bookingobj.getCheckOut(),
                bookingobj.getTotalPayment(),
                bookingobj.getStatus()
        );
    }
}
