package com.hotel.booking.service;

import com.hotel.booking.dto.BookingRequest;
import com.hotel.booking.dto.BookingResponse;
import com.hotel.booking.model.Booking;
import com.hotel.booking.model.Enums.Status;
import com.hotel.booking.model.Hotel;
import com.hotel.booking.model.Room;
import com.hotel.booking.model.User;
import com.hotel.booking.repository.BookingRepository;
import com.hotel.booking.repository.RoomRepository;
import com.hotel.booking.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingrepository;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private CurrentUserService currentuser;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private BookingService bookingService;


    //Helper Function
    private Booking createBooking(Long id) {
        Hotel hotel = new Hotel();
        hotel.setName("Hilton");

        Room room = new Room();
        room.setRoomNumber(101);
        room.setHotel(hotel);

        Booking booking = new Booking();
        booking.setId(id);
        booking.setRoom(room);
        booking.setCheckIn(LocalDate.now());
        booking.setCheckOut(LocalDate.now());
        booking.setTotalPayment(2000);
        booking.setStatus(Status.CONFIRMED);

        return booking;
    }
    private BookingRequest createBookingRequest() {
        return new BookingRequest(
                1L,
                LocalDate.now(),
                LocalDate.now().plusDays(2),
                2,
                2000.0
        );
    }


    //Tests

    @Test
    void getAllBookings_shouldReturnMappedResponses() {
        Booking booking = createBooking(1L);
        when(bookingrepository.findAll()).thenReturn(List.of(booking));

        List<BookingResponse> result = bookingService.getAllBookings();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).hotelName()).isEqualTo("Hilton");
        assertThat(result.get(0).roomNumber()).isEqualTo(101);

        verify(bookingrepository, times(1)).findAll();
    }

    @Test
    void saveBooking_shouldSaveandReturnResponse() {

        BookingRequest request = createBookingRequest();
        Booking savedBooking = createBooking(1L);

        Room room = new Room();
        room.setPrice(2000);

        User user = new User();
        user.setId(1L);

        when(currentuser.getCurrentUserId())
                .thenReturn(1L);

        when(roomRepository.getById(anyLong()))
                .thenReturn(room);

        when(userRepository.findUserById(anyLong()))
                .thenReturn(user);

        when(bookingrepository.save(any(Booking.class)))
                .thenReturn(savedBooking);

        BookingResponse response = bookingService.saveBooking(request);

        assertThat(response.hotelName()).isEqualTo("Hilton");
        assertThat(response.totalPayment()).isEqualTo(2000);

        verify(bookingrepository).save(any(Booking.class));
    }

    @Test
    void getUserBookings_shouldReturnUserBookings() {
        Booking booking = createBooking(2L);
        when(bookingrepository.findByUserId(10L)).thenReturn(List.of(booking));

        List<BookingResponse> responses = bookingService.getUserBookings(10L);

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).status()).isEqualTo(Status.CONFIRMED);

        verify(bookingrepository).findByUserId(10L);
    }

    @Test
    void getBookingbyID_shouldReturnBookingResponse() {
        Booking booking = createBooking(3L);
        when(bookingrepository.getReferenceById(3L)).thenReturn(booking);

        BookingResponse response = bookingService.getBookingbyID(3L);

        assertThat(response.bookingID()).isEqualTo(3L);
        assertThat(response.hotelName()).isEqualTo("Hilton");

        verify(bookingrepository).getReferenceById(3L);
    }

    @Test
    void updateBooking_shouldUpdateAndSaveBooking() {
        Booking existing = createBooking(1L);
        Booking updated = createBooking(null);
        updated.setTotalPayment(3000);
        updated.setStatus(Status.CANCELLED);

        when(bookingrepository.getReferenceById(1L)).thenReturn(existing);
        when(bookingrepository.save(existing)).thenReturn(existing);

        BookingResponse response = bookingService.updateBooking(1L, updated);

        assertThat(response.totalPayment()).isEqualTo(3000);
        assertThat(response.status()).isEqualTo(Status.CANCELLED);

        verify(bookingrepository).save(existing);
    }
}
