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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private CurrentUserService currentUserService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingService bookingService;


    private Booking createBooking(Long id) {

        Hotel hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Hilton");

        Room room = new Room();
        room.setRoomNumber(101);
        room.setHotel(hotel);

        Booking booking = new Booking();
        booking.setId(id);
        booking.setRoom(room);
        booking.setCheckIn(LocalDate.now());
        booking.setCheckOut(LocalDate.now().plusDays(2));
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


    @Test
    void getAllBookings_shouldReturnMappedResponses() {

        Booking booking = createBooking(1L);

        when(bookingRepository.findAll())
                .thenReturn(List.of(booking));

        List<BookingResponse> result =
                bookingService.getAllBookings();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).hotelName()).isEqualTo("Hilton");
        assertThat(result.get(0).roomNumber()).isEqualTo(101);

        verify(bookingRepository).findAll();
    }

    @Test
    void saveBooking_shouldSaveAndReturnResponse() {

        BookingRequest request = createBookingRequest();

        Room room = new Room();
        room.setId(1L);

        Hotel hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Hilton");

        room.setHotel(hotel);
        room.setRoomNumber(101);

        User user = new User();
        user.setId(1L);

        Booking savedBooking = createBooking(1L);

        when(currentUserService.getCurrentUserId())
                .thenReturn(1L);

        when(roomRepository.findById(1L))
                .thenReturn(Optional.of(room));

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(bookingRepository.save(any(Booking.class)))
                .thenReturn(savedBooking);

        BookingResponse response =
                bookingService.saveBooking(request);

        assertThat(response.bookingID()).isEqualTo(1L);
        assertThat(response.hotelName()).isEqualTo("Hilton");
        assertThat(response.totalPayment()).isEqualTo(2000);

        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void getUserBookings_shouldReturnUserBookings() {

        Booking booking = createBooking(2L);

        when(bookingRepository.findByUserId(10L))
                .thenReturn(List.of(booking));

        List<BookingResponse> responses =
                bookingService.getUserBookings(10L);

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).status())
                .isEqualTo(Status.CONFIRMED);

        verify(bookingRepository).findByUserId(10L);
    }

    @Test
    void getBookingById_shouldReturnBookingResponse() {

        Booking booking = createBooking(3L);

        when(bookingRepository.findById(3L))
                .thenReturn(Optional.of(booking));

        BookingResponse response =
                bookingService.getBookingById(3L);

        assertThat(response.bookingID()).isEqualTo(3L);
        assertThat(response.hotelName()).isEqualTo("Hilton");

        verify(bookingRepository).findById(3L);
    }

    @Test
    void updateBookingStatus_shouldUpdateAndSaveBooking() {

        Booking booking = createBooking(1L);

        when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));

        when(bookingRepository.save(any(Booking.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        BookingResponse response =
                bookingService.updateBookingStatus(
                        1L,
                        Status.CANCELLED
                );

        assertThat(response.status())
                .isEqualTo(Status.CANCELLED);

        verify(bookingRepository).save(booking);
    }

    @Test
    void isRoomAvailable_shouldReturnTrueWhenNoOverlap() {

        when(
                bookingRepository.existsOverlappingBooking(
                        1L,
                        LocalDate.now(),
                        LocalDate.now().plusDays(2)
                )
        ).thenReturn(false);

        boolean available =
                bookingService.isRoomAvailable(
                        1L,
                        LocalDate.now(),
                        LocalDate.now().plusDays(2)
                );

        assertThat(available).isTrue();
    }

    @Test
    void getOverlappingBookings_shouldReturnBookings() {

        Booking booking = createBooking(1L);

        when(
                bookingRepository.findOverlappingBookings(
                        anyLong(),
                        any(LocalDate.class),
                        any(LocalDate.class)
                )
        ).thenReturn(List.of(booking));

        List<Booking> result =
                bookingService.getOverlappingBookings(
                        1L,
                        LocalDate.now(),
                        LocalDate.now().plusDays(2)
                );

        assertThat(result).hasSize(1);

        verify(bookingRepository)
                .findOverlappingBookings(
                        anyLong(),
                        any(LocalDate.class),
                        any(LocalDate.class)
                );
    }
}