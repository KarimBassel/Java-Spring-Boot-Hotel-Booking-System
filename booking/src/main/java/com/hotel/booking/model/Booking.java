package com.hotel.booking.model;

import com.hotel.booking.model.Enums.Status;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Optional;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private double basePrice;

    @Column
    private LocalDate checkIn;

    @Column
    private LocalDate checkOut;

    @Column
    private LocalDate createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    //foreign keys for user making the booking and the booked room
    @ManyToOne
    @JoinColumn(name="user_id" , nullable=false)
    private User user;


    @ManyToOne
    @JoinColumn(name="room_id" ,nullable = false)
    private Room room;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private Payment payment;

    public Booking(){}
    public Booking(Room room, User user, Status status, LocalDate createdAt, LocalDate checkOut, LocalDate checkIn, double totalPayment) {
        this.room = room;
        this.user = user;
        this.status = status;
        this.createdAt = createdAt;
        this.checkOut = checkOut;
        this.checkIn = checkIn;
        this.basePrice = totalPayment;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public double getTotalPayment() {
        return basePrice;
    }

    public void setTotalPayment(double totalPayment) {
        this.basePrice = totalPayment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }
}
