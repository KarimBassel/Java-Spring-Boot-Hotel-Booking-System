package com.hotel.booking.auth.controller;

import com.hotel.booking.auth.service.AuthService;
import com.hotel.booking.auth.dto.AuthResponse;
import com.hotel.booking.auth.dto.LoginRequest;
import com.hotel.booking.auth.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    //Constructor injection better than autowired
    //field cannot be final in autowired
    //easier in unit testing --> object is always valid
    //autowired uses reflection feature in java to assign the object to the reference at runtime
    public AuthController(AuthService authService){
        this.authService=authService;
    }

    /*
    @Valid coordinates with the Bean validation annotations like @NotNull,@NotBlank
    and validates the objects automatically using bean validation rules
    if validation fails --> Spring rejects the request and returns 400 Bad Request
     */
    @PostMapping("/register")
    public AuthResponse register(@RequestBody @Valid RegisterRequest request){
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid LoginRequest request){
        return authService.login(request);
    }

}
