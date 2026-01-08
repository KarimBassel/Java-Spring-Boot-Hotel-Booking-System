package com.hotel.booking.model;

import jakarta.persistence.*;

@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "hotel_id" , nullable = false)
    private Hotel hotel;

    @Column
    private double review;
    public Review(){}
    public Review(User user, Hotel hotel, double review) {
        this.user = user;
        this.hotel = hotel;
        this.review = review;
    }


    public double getReview() {
        return review;
    }

    public void setReview(double review) {
        this.review = review;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
