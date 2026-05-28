package com.hotel.booking.service;

import com.hotel.booking.dto.BookingRequest;
import com.hotel.booking.dto.BookingResponse;
import com.hotel.booking.dto.RoomAvailabilityResponse;
import com.hotel.booking.dto.UserResponse;
import com.hotel.booking.model.Booking;
import com.hotel.booking.model.Enums.Status;
import com.hotel.booking.model.User;
import com.hotel.booking.repository.BookingRepository;
import com.hotel.booking.repository.HotelRepository;
import com.hotel.booking.repository.RoomRepository;
import com.hotel.booking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingrepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private UserRepository userrepository;
    @Autowired
    private CurrentUserService currentuser;

    //for testing
    public List<BookingResponse> getAllBookings(){
        List<Booking> list=  bookingrepository.findAll();
        List<BookingResponse> responses = new ArrayList<>();
        for(Booking booking : list){
            responses.add(maptoBookingResponse(booking));
        }
        return responses;
    }
    public BookingResponse saveBooking(BookingRequest bookingRequest){
        Booking booking = new Booking();
        booking.setRoom(roomRepository.getById(bookingRequest.roomId()));
        User user = userrepository.findUserById(currentuser.getCurrentUserId());
        booking.setUser(user);
        booking.setCheckIn(bookingRequest.checkInDate());
        booking.setCheckOut(bookingRequest.checkOutDate());
        booking.setTotalPayment(bookingRequest.totalPrice());
        booking.setStatus(Status.PENDING);
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

    public BookingResponse updateBookingStatus(Long id, Status status){
        //get current booking
        Booking b = bookingrepository.getReferenceById(id);
        if(b != null){
            b.setStatus(status);
            return maptoBookingResponse(bookingrepository.save(b));
        }
        return null;

    }

    public boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut) {

        boolean exists =
                bookingrepository.existsOverlappingBooking(
                        roomId,
                        checkIn,
                        checkOut
                );

        return !exists;
    }

    public List<Booking> getOverlappingBookings(Long roomId, LocalDate checkIn, LocalDate checkOut){
        List<Booking> overlapping = bookingrepository.findOverlappingBookings(roomId,checkIn,checkOut);

        return overlapping;
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
