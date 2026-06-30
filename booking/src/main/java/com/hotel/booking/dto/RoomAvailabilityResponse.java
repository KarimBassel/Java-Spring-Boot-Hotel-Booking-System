package com.hotel.booking.dto;
import java.util.List;
import java.time.LocalDate;

public record RoomAvailabilityResponse(
        Long roomId,
        LocalDate checkIn,
        LocalDate checkOut,
        boolean available,
        List<ConflictPeriod> conflicts
) {}
