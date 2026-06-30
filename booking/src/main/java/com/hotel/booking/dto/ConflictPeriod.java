package com.hotel.booking.dto;
import java.time.LocalDate;

public record ConflictPeriod(
        LocalDate checkIn,
        LocalDate checkOut
) {}