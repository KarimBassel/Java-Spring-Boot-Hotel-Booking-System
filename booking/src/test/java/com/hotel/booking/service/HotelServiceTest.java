package com.hotel.booking.service;

import com.hotel.booking.dto.HotelResponse;
import com.hotel.booking.dto.RoomResponse;
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
import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;


@Profile("test")
@ExtendWith(MockitoExtension.class)
public class HotelServiceTest {
    @Mock
    private HotelRepository hotelrepository;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private HotelService hotelService;

    private Hotel hotel;
    private Room room;

    @BeforeEach
    void setUp() {
        room = new Room();
        room.setId(1L);
        room.setRoomNumber(101);
        room.setRoomType(RoomType.DOUBLE);
        room.setPrice(1500);
        room.setAvailability(true);

        hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Hilton");
        hotel.setLocation("Cairo");
        hotel.setDescription("Luxury hotel");
        hotel.setRooms(List.of(room));
    }


    @Test
    void addHotel_shouldSaveAndReturnHotel() {
        when(hotelrepository.save(hotel)).thenReturn(hotel);

        Hotel saved = hotelService.addHotel(hotel);

        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo("Hilton");
        verify(hotelrepository).save(hotel);
    }


    @Test
    void updateHotel_whenHotelExists_shouldUpdateFields() {
        Hotel updated = new Hotel();
        updated.setName("Updated Name");
        updated.setLocation("Alex");
        updated.setDescription("Updated Desc");

        when(hotelrepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(hotelrepository.save(any(Hotel.class))).thenReturn(hotel);

        Optional<Hotel> result = hotelService.updateHotel(1L, updated);

        assertThat(result).isPresent();
        assertThat(hotel.getName()).isEqualTo("Updated Name");
        assertThat(hotel.getLocation()).isEqualTo("Alex");

        verify(hotelrepository).save(hotel);
    }

    @Test
    void updateHotel_whenHotelNotFound_shouldReturnEmpty() {
        when(hotelrepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Hotel> result = hotelService.updateHotel(1L, hotel);

        assertThat(result).isEmpty();
        verify(hotelrepository, never()).save(any());
    }

    @Test
    void deleteHotel_whenExists_shouldDeleteAndReturnTrue() {
        when(hotelrepository.existsById(1L)).thenReturn(true);

        boolean result = hotelService.deleteHotel(1L);

        assertThat(result).isTrue();
        verify(hotelrepository).deleteById(1L);
    }

    @Test
    void deleteHotel_whenNotExists_shouldReturnFalse() {
        when(hotelrepository.existsById(1L)).thenReturn(false);

        boolean result = hotelService.deleteHotel(1L);

        assertThat(result).isFalse();
        verify(hotelrepository, never()).deleteById(any());
    }

    @Test
    void getHotelById_shouldReturnMappedHotelResponse() {
        when(hotelrepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(hotelrepository.findAverageRating(1L)).thenReturn(4.5);

        HotelResponse response = hotelService.getHotelById(1L);

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Hilton");
        assertThat(response.averageRating()).isEqualTo(4.5);
        assertThat(response.Rooms()).hasSize(1);

        RoomResponse roomResponse = response.Rooms().get(0);
        assertThat(roomResponse.roomNumber()).isEqualTo(101);
    }

    @Test
    void getHotelById_whenNotFound_shouldReturnNull() {
        when(hotelrepository.findById(1L)).thenReturn(Optional.empty());

        HotelResponse response = hotelService.getHotelById(1L);

        assertThat(response).isNull();
    }

    @Test
    void getAllHotels_shouldReturnMappedList() {
        when(hotelrepository.findAll()).thenReturn(List.of(hotel));
        when(hotelrepository.findAverageRating(1L)).thenReturn(3.8);

        List<HotelResponse> responses = hotelService.getAllHotels();

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).name()).isEqualTo("Hilton");
    }

    @Test
    void getAverageRating_shouldCalculateCorrectly() {
        Review r1 = new Review();
        r1.setReview(4);

        Review r2 = new Review();
        r2.setReview(5);

        when(hotelrepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(reviewRepository.findByHotel(hotel)).thenReturn(List.of(r1, r2));

        double avg = hotelService.getAverageRating(1L);

        assertThat(avg).isEqualTo(4.5);
    }

    @Test
    void getAverageRating_whenHotelNotFound_shouldReturnZero() {
        when(hotelrepository.findById(1L)).thenReturn(Optional.empty());

        double avg = hotelService.getAverageRating(1L);

        assertThat(avg).isEqualTo(0.0);
    }

}
