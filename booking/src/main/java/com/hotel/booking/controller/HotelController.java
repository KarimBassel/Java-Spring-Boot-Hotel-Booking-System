package com.hotel.booking.controller;

import java.util.List;

import com.hotel.booking.dto.CreateHotelRequest;
import com.hotel.booking.dto.HotelResponse;
import com.hotel.booking.dto.RoomResponse;
import com.hotel.booking.dto.UpdateHotelRequest;
import com.hotel.booking.model.Hotel;
import com.hotel.booking.model.Room;
import com.hotel.booking.service.HotelService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/hotels")
public class HotelController {


    @Autowired
    private HotelService hotelService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('GUEST')")
    @GetMapping
    public List<HotelResponse> getAllAvailableHotels(){
        return hotelService.getAllHotels();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('GUEST')")
    @GetMapping("/{hotel_id}")
    public HotelResponse getHotel(@PathVariable Long hotel_id){
        return hotelService.getHotelById(hotel_id);
    }


    //for Admins
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public HotelResponse createHotel(@RequestBody CreateHotelRequest createHotelRequest){
        return hotelService.addHotel(createHotelRequest);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public HotelResponse updateHotel(@PathVariable Long id,@RequestBody UpdateHotelRequest updateHotelRequest) throws Exception {
        return hotelService.updateHotel(id, updateHotelRequest);
    }

    //Return 200 OK alongside a success message
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);
        return ResponseEntity.ok("Hotel deleted successfully");
    }
}
