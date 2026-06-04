package com.hotel.booking.controller;

import com.hotel.booking.config.SecurityConfig;
import com.hotel.booking.dto.CreateHotelRequest;
import com.hotel.booking.dto.HotelResponse;
import com.hotel.booking.service.HotelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
401 Unauthorized → user not authenticated.
403 Forbidden → user authenticated but lacks permission.
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = true)
@Import(SecurityConfig.class)
@ActiveProfiles("test")
class HotelControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HotelService hotelService;

    private HotelResponse mockHotelResponse() {
        return new HotelResponse(
                1L,
                "Hilton",
                "Cairo",
                "Luxury hotel in downtown Cairo",
                4.6,
                List.of(),
                "image_url"
        );
    }

    /* ------------------ GET /api/hotels ------------------ */

    @Test
    @WithMockUser(roles = "GUEST")
    void guest_canAccessGetAllHotels() throws Exception {
        when(hotelService.getAllHotels())
                .thenReturn(List.of(mockHotelResponse()));

        mockMvc.perform(get("/api/hotels"))
                .andExpect(status().isOk());
    }

    /* ------------------ GET /api/hotels ------------------ */

    @Test
    @WithMockUser(roles = "ADMIN")
    void admin_canAccessGetAllHotels() throws Exception {
        when(hotelService.getAllHotels())
                .thenReturn(List.of(mockHotelResponse()));

        mockMvc.perform(get("/api/hotels"))
                .andExpect(status().isOk());
    }

    /* ------------------ POST /api/hotels ------------------ */

    @Test
    @WithMockUser(roles = "GUEST")
    void guestCannotCreateHotel() throws Exception {
        mockMvc.perform(post("/api/hotels")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                    "name":"Hilton",
                                    "location":"Cairo",
                                    "description":"Luxury hotel",
                                    "image_url":"image_url"
                                }
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminCanCreateHotel() throws Exception {

        when(hotelService.addHotel(any(CreateHotelRequest.class)))
                .thenReturn(mockHotelResponse());

        mockMvc.perform(post("/api/hotels")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                    "name":"Hilton",
                                    "location":"Cairo",
                                    "description":"Luxury hotel",
                                    "image_url":"image_url"
                                }
                                """))
                .andExpect(status().isOk());
    }

    /* ------------------ GET /api/hotels/{id} ------------------ */

    @Test
    @WithMockUser(roles = "GUEST")
    void guest_canAccessHotelRooms() throws Exception {
        when(hotelService.getHotelById(1L))
                .thenReturn(mockHotelResponse());

        mockMvc.perform(get("/api/hotels/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void admin_canAccessHotelRooms() throws Exception {
        when(hotelService.getHotelById(1L))
                .thenReturn(mockHotelResponse());

        mockMvc.perform(get("/api/hotels/1"))
                .andExpect(status().isOk());
    }
}