package com.hotel.booking.auth;

import com.hotel.booking.auth.dto.AuthResponse;
import com.hotel.booking.auth.dto.LoginRequest;
import com.hotel.booking.auth.dto.RegisterRequest;
import com.hotel.booking.config.PasswordEncoderClass;
import com.hotel.booking.model.Enums.Role;
import com.hotel.booking.model.User;
import com.hotel.booking.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private UserRepository userRepository;
    private final JwtService jwtservice;
    private final PasswordEncoderClass passwordencoder;

    public AuthService(UserRepository userRepository, PasswordEncoderClass passwordencoder, JwtService jwtservice){
        this.userRepository=userRepository;
        this.passwordencoder=passwordencoder;
        this.jwtservice=jwtservice;
    }
    //Basic registration is guest
    //one admin added automatically on system startup
    @Transactional
    public AuthResponse register(RegisterRequest registerRequest){
        User user = new User();
        user.setName(registerRequest.name());
        user.setEmail(registerRequest.email());
        user.setPassword(passwordencoder.passwordEncoder().encode(registerRequest.password()));
        user.setPhoneNumber(registerRequest.phonenumber());
        user.setRole(Role.GUEST);

        userRepository.save(user);

        return new AuthResponse(jwtservice.generateToken(user));
    }

    public AuthResponse login(LoginRequest loginRequest){
        User user = userRepository.findByEmail(loginRequest.email()).orElseThrow(() -> new RuntimeException("Invalid Credentials"));
        //Matches method encodes the raw password sent from frontend --> backend
        //then compares it with the encoded password stored in the database
        /*
        it is normal to send raw passwords from frontend --> backend
        because the communication between frontend and backend happens through HTTPS Protocol
        which encrypts everything (encrypted at transport layer --> raw at application layer)
        */
        //first argument is the one getting encoded so the raw password should be passed first
        if(!passwordencoder.passwordEncoder().matches(loginRequest.password(), user.getPassword())){
            throw new RuntimeException("Invalid Credentials");
        }

        return new AuthResponse(jwtservice.generateToken(user));
    }

}
