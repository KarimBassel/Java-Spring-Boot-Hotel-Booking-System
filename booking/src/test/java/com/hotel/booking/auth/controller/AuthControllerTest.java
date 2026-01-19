package com.hotel.booking.auth.controller;

import com.hotel.booking.config.SecurityConfig;
import com.hotel.booking.auth.dto.AuthResponse;
import com.hotel.booking.auth.dto.LoginRequest;
import com.hotel.booking.auth.dto.RegisterRequest;
import com.hotel.booking.auth.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/*
401 Unauthorized → user not authenticated.
403 Forbidden → user authenticated but lacks permission.
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = true)
@Import(SecurityConfig.class)
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    private AuthResponse mockAuthResponse() {
        return new AuthResponse(
                "mock-jwt-token"
        );
    }

    @Test
    void unauthenticatedUser_canRegister() throws Exception {
        when(authService.register(any(RegisterRequest.class)))
                .thenReturn(mockAuthResponse());

        mockMvc.perform(post("/api/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Karim",
                                  "email": "karim@test.com",
                                  "password": "password123",
                                  "phonenumber": "01000000000"
                                }
                                """))
                .andExpect(status().isOk());
    }



    @Test
    void unauthenticatedUser_canLogin() throws Exception {
        when(authService.login(any(LoginRequest.class)))
                .thenReturn(mockAuthResponse());

        mockMvc.perform(post("/api/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "karim@test.com",
                                  "password": "password123"
                                }
                                """))
                .andExpect(status().isOk());
    }



    @Test
    @WithMockUser
    void authenticatedUser_canStillLogin() throws Exception {
        when(authService.login(any(LoginRequest.class)))
                .thenReturn(mockAuthResponse());

        mockMvc.perform(post("/api/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "karim@test.com",
                                  "password": "password123"
                                }
                                """))
                .andExpect(status().isOk());
    }



    @Test
    @WithMockUser
    void authenticatedUser_canStillRegister() throws Exception {
        when(authService.register(any(RegisterRequest.class)))
                .thenReturn(mockAuthResponse());

        mockMvc.perform(post("/api/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Karim",
                                  "email": "karim@test.com",
                                  "password": "password123",
                                  "phonenumber": "01000000000"
                                }
                                """))
                .andExpect(status().isOk());
    }
}
