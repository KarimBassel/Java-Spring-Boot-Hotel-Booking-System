package com.hotel.booking.model;

import com.hotel.booking.model.Enums.Status;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private double totalPayment;

    @Column
    private Date checkIn;

    @Column
    private Date checkOut;

    @Column
    private Date createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    //foreign keys for user making the booking anf the booked room
    @ManyToOne
    @JoinColumn(name="user_id" , nullable=false)
    private User user;


    @ManyToOne
    @JoinColumn(name="room_id" ,nullable = false)
    private Room room;


    public Booking(){}
    public Booking(Room room, User user, Status status, Date createdAt, Date checkOut, Date checkIn, double totalPayment) {
        this.room = room;
        this.user = user;
        this.status = status;
        this.createdAt = createdAt;
        this.checkOut = checkOut;
        this.checkIn = checkIn;
        this.totalPayment = totalPayment;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(Date checkOut) {
        this.checkOut = checkOut;
    }

    public Date getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(Date checkIn) {
        this.checkIn = checkIn;
    }

    public double getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(double totalPayment) {
        this.totalPayment = totalPayment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
