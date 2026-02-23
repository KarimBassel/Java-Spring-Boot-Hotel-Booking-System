package com.hotel.booking.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Formula;

import java.util.List;

@Entity
@Table(name = "hotels")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String description;
    //External brackets has to be kept
    //this query is injected intom another select query
    @Formula("(SELECT AVG(r.review) FROM reviews r WHERE r.hotel_id = id)")
    private double Rating;

    @Column
    private String imageUrl;

    /*
    mappedBy=hotel --> search in all Room objects and add to the list of Rooms
    the rooms that has the (hotel_id == this.id)
     */
    @OneToMany(mappedBy = "hotel")
    private List<Room> rooms;

    //useful for some statistics in hotels pages
    @OneToMany(mappedBy = "hotel")
    private List<Review> reviews;

    //The NO-ARG constructor is very important
    //used by Hibernate to initialize an object
    //Hibernate uses Reflection feature in Java
    //Reflection --> the ability to access class names of attributes,methods etc... in runtime
    public Hotel(){}
    public Hotel(String name, String location, String description, double rating, List<Room> rooms, List<Review> reviews) {
        this.name = name;
        this.location = location;
        this.description = description;
        Rating = rating;
        this.rooms = rooms;
        this.reviews = reviews;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public double getRating() {
        return Rating;
    }

    public void setRating(double rating) {
        Rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
