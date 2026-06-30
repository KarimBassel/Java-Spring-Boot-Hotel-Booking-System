package com.hotel.booking.exception;

public class ReviewNotFoundException extends RuntimeException {
    public ReviewNotFoundException(String resource, Object hotelID, Object userID) {
        super(resource + " not found for Hotel ID: " + hotelID+" and User ID: "+userID);
    }
}
