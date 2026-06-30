package com.hotel.booking.service;
import com.hotel.booking.dto.BookingRequest;
import com.hotel.booking.dto.BookingResponse;
import com.hotel.booking.exception.ResourceNotFoundException;
import com.hotel.booking.model.Booking;
import com.hotel.booking.model.Enums.Status;
import com.hotel.booking.model.User;
import com.hotel.booking.repository.BookingRepository;
import com.hotel.booking.repository.RoomRepository;
import com.hotel.booking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    public BookingResponse saveBooking(BookingRequest request) {

        Booking booking = new Booking();

        booking.setRoom(
                roomRepository.findById(request.roomId())
                        .orElseThrow(() -> new ResourceNotFoundException("Room" , request.roomId()))
        );

        User user = userRepository.findById(
                currentUserService.getCurrentUserId()
        ).orElseThrow(() -> new ResourceNotFoundException("User" , currentUserService.getCurrentUserId()));

        booking.setUser(user);
        booking.setCheckIn(request.checkInDate());
        booking.setCheckOut(request.checkOutDate());
        booking.setCreatedAt(LocalDate.now());
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
                .orElseThrow(() -> new ResourceNotFoundException("Booking", id));

        return mapToBookingResponse(booking);
    }

    public BookingResponse updateBookingStatus(Long id, Status status) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking" , id));

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