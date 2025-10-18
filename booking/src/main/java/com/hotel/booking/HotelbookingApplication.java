package com.hotel.booking;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import com.hotel.booking.model.Hotel;
import com.hotel.booking.repository.HotelRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HotelbookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelbookingApplication.class, args);
	}

	@Bean
	CommandLineRunner run(HotelRepository hotelRepo) {
		return args -> {
			hotelRepo.save(new Hotel("Grand Nile Tower", "Cairo"));
			hotelRepo.save(new Hotel("Marriott Zamalek", "Cairo"));
			System.out.println("✅ Hotels added successfully!");
		};
	}

}
