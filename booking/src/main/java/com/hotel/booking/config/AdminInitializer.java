package com.hotel.booking.config;

import com.hotel.booking.model.Enums.Role;
import com.hotel.booking.model.User;
import com.hotel.booking.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Profile("!test")
@Configuration
public class AdminInitializer {

    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository,
                                PasswordEncoder passwordEncoder) {

        return args -> {
            if (!userRepository.existsByEmail("admin@hotel.com")) {

                User admin = new User();
                admin.setName("System Admin");
                admin.setEmail("admin@hotel.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(Role.ADMIN);

                userRepository.save(admin);

                System.out.println("Default admin user created");
            }
        };
    }
}
