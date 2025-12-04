package com.hotel.booking.service;

import com.hotel.booking.model.Room;
import com.hotel.booking.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private MockPaymentService paymentService;

    public Room saveRoom(Room room) {
        return roomRepository.save(room);
    }


    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id);
    }


    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }


    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }

    public void bookRoom(Long bookingId, Long amount) {
        // Process payment using the mock service
        String paymentId = paymentService.processPayment(bookingId, amount);
        System.out.println("Booking confirmed. Payment ID: " + paymentId);
    }

}
