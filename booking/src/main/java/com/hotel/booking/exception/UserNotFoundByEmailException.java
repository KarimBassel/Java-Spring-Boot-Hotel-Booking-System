package com.hotel.booking.exception;

public class UserNotFoundByEmailException extends RuntimeException {
    public UserNotFoundByEmailException(Object email) {
        super("User not found for email: "+email);
    }
}
