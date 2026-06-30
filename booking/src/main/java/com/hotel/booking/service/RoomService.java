package com.hotel.booking.service;
import com.hotel.booking.dto.CreateRoomRequest;
import com.hotel.booking.dto.RoomResponse;
import com.hotel.booking.dto.UpdateRoomRequest;
import com.hotel.booking.exception.ResourceNotFoundException;
import com.hotel.booking.model.Room;
import com.hotel.booking.model.Hotel;
import com.hotel.booking.repository.RoomRepository;
import com.hotel.booking.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;



    public RoomResponse createRoom(CreateRoomRequest request) {

        Hotel hotel = hotelRepository.findById(request.hotelID())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Hotel", request.hotelID()));

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


    public List<RoomResponse> getAllRooms() {
        return roomRepository.findAll()
                .stream()
                .map(this::mapToRoomResponse)
                .toList();
    }


    public RoomResponse getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Room" , id));

        return mapToRoomResponse(room);
    }


    public List<RoomResponse> getRoomsByHotel(Long hotelId) {
        return roomRepository.findAll()
                .stream()
                .filter(room -> room.getHotel() != null &&
                        room.getHotel().getId().equals(hotelId))
                .map(this::mapToRoomResponse)
                .toList();
    }


    public RoomResponse updateRoom(Long id, UpdateRoomRequest request) {

        Room room = roomRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Room",id));

        room.setRoomNumber(request.roomNumber());
        room.setRoomType(request.roomType());
        room.setPrice(request.price());
        room.setAvailability(request.availability());
        room.setImageUrl(request.imageURL());

        Room saved = roomRepository.save(room);

        return mapToRoomResponse(saved);
    }

    public void deleteRoom(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new ResourceNotFoundException("Room" ,id);
        }
        roomRepository.deleteById(id);
    }

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