package com.hotel.booking.service;

import com.hotel.booking.dto.PaymentIntentResponse;
import com.hotel.booking.model.Room;
import com.hotel.booking.model.Enums.Status;
import com.hotel.booking.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private MockPaymentService paymentService;

    @InjectMocks
    private RoomService roomService;

    private Room room;

    @BeforeEach
    void setup() {
        room = new Room();
        room.setId(1L);
        room.setRoomNumber(101);
        room.setAvailability(true);
    }


    @Test
    void saveRoom_savesAndReturnsRoom() {
        when(roomRepository.save(room)).thenReturn(room);

        Room result = roomService.saveRoom(room);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(roomRepository).save(room);
    }


    @Test
    void getRoomById_whenExists_returnsRoom() {
        when(roomRepository.findById(1L))
                .thenReturn(Optional.of(room));

        Optional<Room> result = roomService.getRoomById(1L);

        assertTrue(result.isPresent());
        assertEquals(101, result.get().getRoomNumber());
    }

    @Test
    void getRoomById_whenNotExists_returnsEmpty() {
        when(roomRepository.findById(1L))
                .thenReturn(Optional.empty());

        Optional<Room> result = roomService.getRoomById(1L);

        assertFalse(result.isPresent());
    }



    @Test
    void getAllRooms_returnsRoomList() {
        when(roomRepository.findAll())
                .thenReturn(List.of(room));

        List<Room> rooms = roomService.getAllRooms();

        assertEquals(1, rooms.size());
        verify(roomRepository).findAll();
    }


    @Test
    void deleteRoom_deletesById() {
        doNothing().when(roomRepository).deleteById(1L);

        roomService.deleteRoom(1L);

        verify(roomRepository).deleteById(1L);
    }


    @Test
    void bookRoom_callsPaymentService() {
        PaymentIntentResponse mockResponse =
                new PaymentIntentResponse(
                        "MOCK_PI_1",
                        "MOCK_CLIENT_SECRET_1",
                        Status.PENDING
                );

        when(paymentService.createPaymentIntent(1L, 500L))
                .thenReturn(mockResponse);

        roomService.bookRoom(1L, 500L);

        verify(paymentService)
                .createPaymentIntent(1L, 500L);
    }
}
