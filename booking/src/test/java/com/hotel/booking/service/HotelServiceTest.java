package com.hotel.booking.service;

import com.hotel.booking.dto.CreateHotelRequest;
import com.hotel.booking.dto.HotelResponse;
import com.hotel.booking.dto.RoomResponse;
import com.hotel.booking.dto.UpdateHotelRequest;
import com.hotel.booking.exception.ResourceNotFoundException;
import com.hotel.booking.model.Enums.RoomType;
import com.hotel.booking.model.Hotel;
import com.hotel.booking.model.Review;
import com.hotel.booking.model.Room;
import com.hotel.booking.repository.HotelRepository;
import com.hotel.booking.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private HotelService hotelService;

    private Hotel hotel;
    private Room room;

    @BeforeEach
    void setUp() {

        hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Hilton");
        hotel.setLocation("Cairo");
        hotel.setDescription("Luxury hotel");
        hotel.setImageUrl("image.jpg");

        room = new Room();
        room.setId(1L);
        room.setRoomNumber(101);
        room.setRoomType(RoomType.DOUBLE);
        room.setPrice(1500);
        room.setAvailability(true);
        room.setHotel(hotel);

        hotel.setRooms(List.of(room));
    }

    @Test
    void addHotel_shouldSaveAndReturnHotel() {

        CreateHotelRequest request =
                new CreateHotelRequest(
                        "Hilton",
                        "Cairo",
                        "Luxury hotel",
                        "image.jpg"
                );

        when(hotelRepository.save(any(Hotel.class)))
                .thenAnswer(invocation -> {
                    Hotel hotel = invocation.getArgument(0);
                    hotel.setId(1L);
                    return hotel;
                });

        when(hotelRepository.findAverageRating(1L))
                .thenReturn(0.0);

        HotelResponse response =
                hotelService.addHotel(request);

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Hilton");
        assertThat(response.location()).isEqualTo("Cairo");
        assertThat(response.description()).isEqualTo("Luxury hotel");

        verify(hotelRepository).save(any(Hotel.class));
    }

    @Test
    void updateHotel_whenHotelExists_shouldUpdateFields() throws Exception {

        UpdateHotelRequest request =
                new UpdateHotelRequest(
                        "Updated Name",
                        "Alex",
                        "Updated Desc",
                        "updated.jpg"
                );

        when(hotelRepository.findById(1L))
                .thenReturn(Optional.of(hotel));

        when(hotelRepository.save(any(Hotel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(hotelRepository.findAverageRating(1L))
                .thenReturn(4.5);

        HotelResponse response =
                hotelService.updateHotel(1L, request);

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Updated Name");
        assertThat(response.location()).isEqualTo("Alex");
        assertThat(response.description()).isEqualTo("Updated Desc");

        verify(hotelRepository).save(hotel);
    }

    @Test
    void updateHotel_whenHotelNotFound_shouldThrowException() {

        UpdateHotelRequest request =
                new UpdateHotelRequest(
                        "Updated Name",
                        "Alex",
                        "Updated Desc",
                        "updated.jpg"
                );

        when(hotelRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> hotelService.updateHotel(1L, request)
        )
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Hotel not found: ");

        verify(hotelRepository, never())
                .save(any());
    }

    @Test
    void deleteHotel_whenExists_shouldDeleteAndReturnTrue() {

        when(hotelRepository.existsById(1L))
                .thenReturn(true);

        boolean result =
                hotelService.deleteHotel(1L);

        assertThat(result).isTrue();

        verify(hotelRepository)
                .deleteById(1L);
    }

    @Test
    void deleteHotel_whenNotExists_shouldReturnFalse() {

        when(hotelRepository.existsById(1L))
                .thenReturn(false);

        boolean result =
                hotelService.deleteHotel(1L);

        assertThat(result).isFalse();

        verify(hotelRepository, never())
                .deleteById(any());
    }

    @Test
    void getHotelById_shouldReturnMappedHotelResponse() {

        when(hotelRepository.findById(1L))
                .thenReturn(Optional.of(hotel));

        when(hotelRepository.findAverageRating(1L))
                .thenReturn(4.5);

        HotelResponse response =
                hotelService.getHotelById(1L);

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Hilton");
        assertThat(response.averageRating()).isEqualTo(4.5);
        assertThat(response.Rooms()).hasSize(1);

        RoomResponse roomResponse =
                response.Rooms().get(0);

        assertThat(roomResponse.roomNumber())
                .isEqualTo(101);
    }

    @Test
    void getHotelById_whenNotFound_shouldThrowException() {

        when(hotelRepository.findById(1L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> hotelService.getHotelById(1L)
        );

        assertEquals("Hotel not found: 1", ex.getMessage());
    }

    @Test
    void getAllHotels_shouldReturnMappedList() {

        when(hotelRepository.findAll())
                .thenReturn(List.of(hotel));

        when(hotelRepository.findAverageRating(1L))
                .thenReturn(3.8);

        List<HotelResponse> responses =
                hotelService.getAllHotels();

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).name()).isEqualTo("Hilton");
        assertThat(responses.get(0).averageRating()).isEqualTo(3.8);
    }

    @Test
    void getAverageRating_shouldCalculateCorrectly() {

        Review review1 = new Review();
        review1.setReview(4);

        Review review2 = new Review();
        review2.setReview(5);

        when(hotelRepository.findById(1L))
                .thenReturn(Optional.of(hotel));

        when(reviewRepository.findByHotel(hotel))
                .thenReturn(List.of(review1, review2));

        double average =
                hotelService.getAverageRating(1L);

        assertThat(average).isEqualTo(4.5);
    }

    @Test
    void getAverageRating_whenHotelNotFound_shouldReturnZero() {

        when(hotelRepository.findById(1L))
                .thenReturn(Optional.empty());

        double average =
                hotelService.getAverageRating(1L);

        assertThat(average).isEqualTo(0.0);
    }
}