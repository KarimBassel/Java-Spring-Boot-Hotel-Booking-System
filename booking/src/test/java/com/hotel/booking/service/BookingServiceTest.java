package com.hotel.booking.service;

import com.hotel.booking.dto.BookingResponse;
import com.hotel.booking.model.Booking;
import com.hotel.booking.model.Enums.Status;
import com.hotel.booking.model.Hotel;
import com.hotel.booking.model.Room;
import com.hotel.booking.repository.BookingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Date;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingrepository;

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
        booking.setCheckIn(new Date());
        booking.setCheckOut(new Date());
        booking.setTotalPayment(2000);
        booking.setStatus(Status.CONFIRMED);

        return booking;
    }


    //Tests

    @Test
    void getAllBookings_shouldReturnMappedResponses() {
        Booking booking = createBooking(1L);
        when(bookingrepository.findAll()).thenReturn(List.of(booking));

        List<BookingResponse> result = bookingService.getAllBookings();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).hotelName()).isEqualTo("Hilton");
        assertThat(result.get(0).rommNumber()).isEqualTo(101);

        verify(bookingrepository, times(1)).findAll();
    }

    @Test
    void saveBooking_shouldSaveandReturnResponse(){
        Booking booking = createBooking(1L);
        when(bookingrepository.save(booking)).thenReturn(booking);

        BookingResponse response = bookingService.saveBooking(booking);

        assertThat(response.hotelName()).isEqualTo("Hilton");
        assertThat(response.totalPayment()).isEqualTo(2000);

        //verifies that this method is called during the tests
        verify(bookingrepository).save(booking);
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
