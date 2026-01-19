package com.hotel.booking.model;

import com.hotel.booking.model.Enums.RoomType;
import jakarta.persistence.*;

@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int roomNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomType roomType;

    @Column(nullable = false)
    private double price;

    @Column
    private boolean availability;

    /*
    hotel_id --> foreign key that will be added to rooms table
    hotel_id linked with the primary key found in table hotels which is the id
    */
    @ManyToOne
    @JoinColumn(name="hotel_id" , nullable = false)
    private Hotel hotel;


    public Room() {

    }

    public Room(boolean availability, double price, RoomType roomType, int roomNumber) {
        this.availability = availability;
        this.price = price;
        this.roomType = roomType;
        this.roomNumber = roomNumber;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public double getPrice() {
        return price;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
