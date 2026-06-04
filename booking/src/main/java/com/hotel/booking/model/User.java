package com.hotel.booking.model;

import com.hotel.booking.model.Enums.Role;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;


@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(length = 15)
    private String phoneNumber;

    @Column
    private String imageUrl;
    
    //User is active by default on creation
    @Column
    private boolean status=true;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @OneToMany(mappedBy = "user")
    private List<Review> userReviews;

    @OneToMany(mappedBy = "user")
    private List<Booking> userBookings;

    public User() {}

    public User(List<Booking> userBookings, List<Review> userReviews, String phoneNumber, Role role, String password, String email, String name, boolean status) {
        this.userBookings = userBookings;
        this.userReviews = userReviews;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.password = password;
        this.email = email;
        this.name = name;
        this.status = status;
    }

    public List<Review> getUsers() {
        return userReviews;
    }

    public void setUsers(List<Review> usersrevs) {
        this.userReviews = usersrevs;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public List<Booking> getUserBookings() {
        return userBookings;
    }

    public void setUserBookings(List<Booking> userBookings) {
        this.userBookings = userBookings;
    }

    public List<Review> getUserReviews() {
        return userReviews;
    }

    public void setUserReviews(List<Review> userReviews) {
        this.userReviews = userReviews;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
