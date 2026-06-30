package com.hotel.booking.exception;

public class ReviewAlreadyExistsException extends RuntimeException {

    public ReviewAlreadyExistsException() {
        super("Review Already Exists");
    }
}