package com.hotel.booking.service;
import com.hotel.booking.dto.CreateRoomRequest;
import com.hotel.booking.dto.RoomResponse;
import com.hotel.booking.dto.UpdateRoomRequest;
import com.hotel.booking.model.Enums.RoomType;
import com.hotel.booking.model.Hotel;
import com.hotel.booking.model.Room;
import com.hotel.booking.repository.HotelRepository;
import com.hotel.booking.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private RoomService roomService;

    private Hotel hotel;
    private Room room;

    @BeforeEach
    void setup() {

        hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Hilton");

        room = new Room();
        room.setId(1L);
        room.setRoomNumber(101);
        room.setRoomType(RoomType.DOUBLE);
        room.setPrice(1500);
        room.setAvailability(true);
        room.setImageUrl("room.jpg");
        room.setHotel(hotel);
    }

    @Test
    void createRoom_shouldSaveAndReturnResponse() {

        CreateRoomRequest request =
                new CreateRoomRequest(
                        true,
                        1500,
                        101,
                        RoomType.DOUBLE,
                        1L,
                        "room.jpg"
                );

        when(hotelRepository.findById(1L))
                .thenReturn(Optional.of(hotel));

        when(roomRepository.save(any(Room.class)))
                .thenReturn(room);

        RoomResponse response =
                roomService.createRoom(request);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.roomNumber()).isEqualTo(101);
        assertThat(response.roomtype()).isEqualTo(RoomType.DOUBLE);
        assertThat(response.price()).isEqualTo(1500);
        assertThat(response.availability()).isTrue();
        assertThat(response.hotelID()).isEqualTo(1L);
        assertThat(response.hotelName()).isEqualTo("Hilton");

        verify(roomRepository).save(any(Room.class));
    }

    @Test
    void createRoom_shouldThrowWhenHotelNotFound() {

        CreateRoomRequest request =
                new CreateRoomRequest(
                        true,
                        1500,
                        101,
                        RoomType.DOUBLE,
                        99L,
                        "room.jpg"
                );

        when(hotelRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                roomService.createRoom(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Hotel not found");
    }

    @Test
    void getRoomById_shouldReturnMappedResponse() {

        when(roomRepository.findById(1L))
                .thenReturn(Optional.of(room));

        RoomResponse response =
                roomService.getRoomById(1L);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.roomNumber()).isEqualTo(101);
        assertThat(response.roomtype()).isEqualTo(RoomType.DOUBLE);
        assertThat(response.hotelName()).isEqualTo("Hilton");

        verify(roomRepository).findById(1L);
    }

    @Test
    void getRoomById_shouldThrowWhenNotFound() {

        when(roomRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                roomService.getRoomById(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Room not found");
    }

    @Test
    void getAllRooms_shouldReturnMappedResponses() {

        when(roomRepository.findAll())
                .thenReturn(List.of(room));

        List<RoomResponse> responses =
                roomService.getAllRooms();

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).roomNumber())
                .isEqualTo(101);

        verify(roomRepository).findAll();
    }

    @Test
    void getRoomsByHotel_shouldReturnMatchingRooms() {

        when(roomRepository.findAll())
                .thenReturn(List.of(room));

        List<RoomResponse> responses =
                roomService.getRoomsByHotel(1L);

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).hotelID())
                .isEqualTo(1L);

        verify(roomRepository).findAll();
    }

    @Test
    void getRoomsByHotel_shouldReturnEmptyListWhenNoRoomsMatch() {

        when(roomRepository.findAll())
                .thenReturn(List.of(room));

        List<RoomResponse> responses =
                roomService.getRoomsByHotel(999L);

        assertThat(responses).isEmpty();
    }

    @Test
    void updateRoom_shouldUpdateAndSaveRoom() {

        UpdateRoomRequest request =
                new UpdateRoomRequest(
                        1L,
                        false,
                        3000,
                        202,
                        RoomType.SUITE,
                        1L,
                        "updated.jpg"
                );

        when(roomRepository.findById(1L))
                .thenReturn(Optional.of(room));

        when(roomRepository.save(any(Room.class)))
                .thenReturn(room);

        RoomResponse response =
                roomService.updateRoom(1L, request);

        assertThat(response.roomNumber()).isEqualTo(202);
        assertThat(response.roomtype()).isEqualTo(RoomType.SUITE);
        assertThat(response.price()).isEqualTo(3000);
        assertThat(response.availability()).isFalse();

        verify(roomRepository).save(room);
    }

    @Test
    void updateRoom_shouldThrowWhenRoomNotFound() {

        UpdateRoomRequest request =
                new UpdateRoomRequest(
                        1L,
                        false,
                        3000,
                        202,
                        RoomType.SUITE,
                        1L,
                        "updated.jpg"
                );

        when(roomRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                roomService.updateRoom(1L, request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Room not found");
    }

    @Test
    void deleteRoom_shouldDeleteWhenExists() {

        when(roomRepository.existsById(1L))
                .thenReturn(true);

        roomService.deleteRoom(1L);

        verify(roomRepository).deleteById(1L);
    }

    @Test
    void deleteRoom_shouldThrowWhenRoomNotFound() {

        when(roomRepository.existsById(1L))
                .thenReturn(false);

        assertThatThrownBy(() ->
                roomService.deleteRoom(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Room not found");
    }

}