package com.hotel.booking.service;

import com.hotel.booking.dto.CreateRoomRequest;
import com.hotel.booking.dto.RoomResponse;
import com.hotel.booking.dto.UpdateRoomRequest;
import com.hotel.booking.model.Room;
import com.hotel.booking.model.Hotel;
import com.hotel.booking.repository.RoomRepository;
import com.hotel.booking.repository.HotelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final MockPaymentService paymentService;

    public RoomService(RoomRepository roomRepository,
                       HotelRepository hotelRepository,
                       MockPaymentService paymentService) {
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
        this.paymentService = paymentService;
    }

    // =========================
    // CREATE ROOM
    // =========================
    public RoomResponse createRoom(CreateRoomRequest request) {

        Hotel hotel = hotelRepository.findById(request.hotelID())
                .orElseThrow(() ->
                        new RuntimeException("Hotel not found with id: " + request.hotelID()));

        Room room = new Room();
        room.setRoomNumber(request.roomNumber());
        room.setRoomType(request.roomType());
        room.setPrice(request.price());
        room.setAvailability(request.availability());
        room.setImageUrl(request.imageURL());
        room.setHotel(hotel);

        Room saved = roomRepository.save(room);

        return mapToRoomResponse(saved);
    }

    // =========================
    // GET ALL ROOMS
    // =========================
    public List<RoomResponse> getAllRooms() {
        return roomRepository.findAll()
                .stream()
                .map(this::mapToRoomResponse)
                .toList();
    }

    // =========================
    // GET ROOM BY ID
    // =========================
    public RoomResponse getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Room not found with id: " + id));

        return mapToRoomResponse(room);
    }

    // =========================
    // GET ROOMS BY HOTEL (MISSING BEFORE)
    // =========================
    public List<RoomResponse> getRoomsByHotel(Long hotelId) {
        return roomRepository.findAll()
                .stream()
                .filter(room -> room.getHotel() != null &&
                        room.getHotel().getId().equals(hotelId))
                .map(this::mapToRoomResponse)
                .toList();
    }

    // =========================
    // UPDATE ROOM
    // =========================
    public RoomResponse updateRoom(Long id, UpdateRoomRequest request) {

        Room room = roomRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Room not found with id: " + id));

        room.setRoomNumber(request.roomNumber());
        room.setRoomType(request.roomType());
        room.setPrice(request.price());
        room.setAvailability(request.availability());
        room.setImageUrl(request.imageURL());

        Room saved = roomRepository.save(room);

        return mapToRoomResponse(saved);
    }

    // =========================
    // DELETE ROOM
    // =========================
    public void deleteRoom(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new RuntimeException("Room not found with id: " + id);
        }
        roomRepository.deleteById(id);
    }

    // =========================
    // BOOK ROOM (your existing logic)
    // =========================
    public void bookRoom(Long bookingId, Long amount) {
        var payment = paymentService.createPaymentIntent(bookingId, amount);
        System.out.println("Booking confirmed. Payment ID: " + payment);
    }

    // =========================
    // MAPPER
    // =========================
    private RoomResponse mapToRoomResponse(Room room) {
        return new RoomResponse(
                room.getId(),
                room.getRoomNumber(),
                room.getRoomType(),
                room.getPrice(),
                room.isAvailability(),
                room.getImageUrl(),
                room.getHotel().getId(),
                room.getHotel().getName()
        );
    }
}