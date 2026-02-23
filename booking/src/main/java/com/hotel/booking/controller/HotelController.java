package com.hotel.booking.controller;

import java.util.List;

import com.hotel.booking.dto.HotelResponse;
import com.hotel.booking.dto.RoomResponse;
import com.hotel.booking.model.Hotel;
import com.hotel.booking.model.Room;
import com.hotel.booking.service.HotelService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/hotels")
public class HotelController {


    @Autowired
    private HotelService hotelService;
    @GetMapping
    public List<HotelResponse> getAllAvailableHotels(){
        return hotelService.getAllHotels();
    }

    @GetMapping("/{hotel_id}")
    public HotelResponse getHotel(@PathVariable Long hotel_id){
        return hotelService.getHotelById(hotel_id);
    }
    //for Admins
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Hotel createHotel(@RequestBody Hotel hotel){
        return hotelService.addHotel(hotel);
    }

//    @GetMapping("/{hotel_id}")
//    public List<RoomResponse> getHotelRooms(@PathVariable Long hotel_id){
//        HotelResponse hotel = hotelService.getHotelById(hotel_id);
//        return hotel.Rooms();
//    }
}
