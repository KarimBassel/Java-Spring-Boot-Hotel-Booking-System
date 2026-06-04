package com.hotel.booking.service;

import com.hotel.booking.dto.BookingRequest;
import com.hotel.booking.dto.BookingResponse;
import com.hotel.booking.dto.UpdateBookingRequest;
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
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CurrentUserService currentUserService;

    public BookingResponse saveBooking(BookingRequest request) {

        Booking booking = new Booking();

        booking.setRoom(
                roomRepository.findById(request.roomId())
                        .orElseThrow(() -> new RuntimeException("Room not found"))
        );

        User user = userRepository.findById(
                currentUserService.getCurrentUserId()
        ).orElseThrow(() -> new RuntimeException("User not found"));

        booking.setUser(user);
        booking.setCheckIn(request.checkInDate());
        booking.setCheckOut(request.checkOutDate());
        booking.setTotalPayment(request.totalPrice());
        booking.setStatus(Status.PENDING);

        return mapToBookingResponse(bookingRepository.save(booking));
    }

    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(this::mapToBookingResponse)
                .toList();
    }

    public List<BookingResponse> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId)
                .stream()
                .map(this::mapToBookingResponse)
                .toList();
    }

    public BookingResponse getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        return mapToBookingResponse(booking);
    }

    public BookingResponse updateBookingStatus(Long id, Status status) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setStatus(status);

        return mapToBookingResponse(bookingRepository.save(booking));
    }


    public boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        return !bookingRepository.existsOverlappingBooking(roomId, checkIn, checkOut);
    }

    public List<Booking> getOverlappingBookings(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        return bookingRepository.findOverlappingBookings(roomId, checkIn, checkOut);
    }

    private BookingResponse mapToBookingResponse(Booking b) {
        return new BookingResponse(
                b.getId(),
                b.getRoom().getHotel().getId(),
                b.getRoom().getHotel().getName(),
                b.getRoom().getRoomNumber(),
                b.getCheckIn(),
                b.getCheckOut(),
                b.getTotalPayment(),
                b.getStatus()
        );
    }
}