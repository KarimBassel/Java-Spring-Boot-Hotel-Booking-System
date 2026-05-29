package com.hotel.booking.controller;

import com.hotel.booking.config.SecurityConfig;
import com.hotel.booking.dto.BookingRequest;
import com.hotel.booking.dto.BookingResponse;
import com.hotel.booking.model.Booking;
import com.hotel.booking.model.Enums.Status;
import com.hotel.booking.security.BookingSecurity;
import com.hotel.booking.service.BookingService;
import net.bytebuddy.asm.Advice;
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
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

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
class BookingControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingSecurity bookingSecurity;

    @MockBean
    private BookingService bookingService;

    private BookingResponse mockBookingResponse() {
        return new BookingResponse(
                1L,
                2L,
                "Hilton",
                101,
                LocalDate.now(),
                LocalDate.now(),
                2500.0,
                Status.CONFIRMED
        );
    }

    /* ------------------ POST /api/bookings ------------------ */

    @Test
    @WithMockUser(roles = "GUEST")
    void authenticatedUser_canCreateBooking() throws Exception {
        when(bookingService.saveBooking(any(BookingRequest.class)))
                .thenReturn(mockBookingResponse());

        mockMvc.perform(post("/api/bookings")
                        .contentType(APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());
    }



    @Test
    @WithMockUser(roles = "GUEST")
    void guestCannotAccessGetAllBookings() throws Exception {
        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminCanAccessGetAllBookings() throws Exception {
        when(bookingService.getAllBookings())
                .thenReturn(List.of(mockBookingResponse()));

        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(roles = "GUEST")
    void guestNotOwner_cannotUpdateBooking() throws Exception {

        when(bookingSecurity.IsBookingOwner(1L)).thenReturn(false);

        mockMvc.perform(put("/api/bookings/1")
                        .contentType(APPLICATION_JSON)
                        .content("\"CONFIRMED\""))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "GUEST")
    void owner_canUpdateBooking() throws Exception {
        when(bookingSecurity.IsBookingOwner(1L)).thenReturn(true);
        when(bookingService.updateBookingStatus(1L, Status.CONFIRMED))
                .thenReturn(mockBookingResponse());

        mockMvc.perform(put("/api/bookings/1")
                        .contentType(APPLICATION_JSON)
                        .content("\"CONFIRMED\""))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void admin_canUpdateBooking_evenIfNotOwner() throws Exception {

        when(bookingSecurity.IsBookingOwner(1L)).thenReturn(false);
        when(bookingService.updateBookingStatus(1L, Status.CONFIRMED))
                .thenReturn(mockBookingResponse());

        mockMvc.perform(put("/api/bookings/1")
                        .contentType(APPLICATION_JSON)
                        .content("\"CONFIRMED\""))
                .andExpect(status().isOk());
    }
}
