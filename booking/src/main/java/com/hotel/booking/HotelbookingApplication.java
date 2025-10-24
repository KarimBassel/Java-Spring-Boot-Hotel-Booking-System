package com.hotel.booking;
import com.hotel.booking.model.*;
import com.hotel.booking.model.Enums.RoomType;
import com.hotel.booking.model.Enums.Status;
import com.hotel.booking.repository.BookingRepository;
import com.hotel.booking.repository.RoomRepository;
import com.hotel.booking.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

@SpringBootApplication
public class HotelbookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelbookingApplication.class, args);
	}

	@Bean
	CommandLineRunner run(BookingRepository bookingRepo, UserRepository userRepo, RoomRepository RoomRepo) {
		return args -> {
			User u = userRepo.getReferenceById(1L);
			RoomRepo.save(new Room(true , 2000 , RoomType.SUITE, 1));
			Room r = RoomRepo.getReferenceById(1L);
			bookingRepo.save(new Booking(r, u, Status.PENDING, new Date(2025,11,3) , new Date(2025,11,3), new Date(2025,11,3), 3000));
			//System.out.println("Bookings added successfully!");
		};
	}

}
