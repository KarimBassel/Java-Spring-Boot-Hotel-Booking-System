package com.hotel.booking.controller;

import java.util.List;
import com.hotel.booking.model.Hotel;
import com.hotel.booking.model.Room;
import com.hotel.booking.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {


    @Autowired
    private HotelService hotelService;
    @GetMapping
    public List<Hotel> getAllAvailableHotels(){
        return hotelService.getAllHotels();
    }
    //for Admins
    @PostMapping
    public Hotel createHotel(@RequestBody Hotel hotel){
        return hotelService.addHotel(hotel);
    }

    @GetMapping("/{hotel_id}")
    public List<Room> getHotelRooms(@PathVariable Long hotel_id){
        Hotel hotel = hotelService.getHotelById(hotel_id);
        return hotel.getRooms();
    }
}
