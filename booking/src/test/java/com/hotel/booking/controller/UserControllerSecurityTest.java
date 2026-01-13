package com.hotel.booking.controller;
import com.hotel.booking.dto.UserResponse;
import com.hotel.booking.model.Enums.Role;
import com.hotel.booking.security.UserSecurity;
import com.hotel.booking.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
WithMockUser annotation creates a mock authentication object
and keeps it temporarily in the SecurityContext for the duration of the test
 */
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerSecurityTest {

    //this triggers the filter chain before executing the tests
    @Autowired
    private MockMvc mockMvc;

    @Test
    /*
    Spring creates an Authentication Object
    Authentication:
        - principal = "user"
        - role = ROLE_GUEST
     */
    @WithMockUser(roles = "GUEST")
    void guestCannotAccessGetAllUsers() throws Exception {
        //Must return 401
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminCanAccessGetAllUsers() throws Exception {
        //Must return 200
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk());
    }

    @Test
    void unauthenticatedUser_cannotAccess() throws Exception {
        //Must return 401
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isForbidden());
    }

//    @Test
//    @WithMockUser(roles = "GUEST")
//    void guestNotOwner_cannotAccess() throws Exception {
//        //Whenever the method isOwner(1L) is called on this mock, return false instead of executing the real logic.
//        when(userSecurity.isOwner(1L)).thenReturn(false);
//
//        mockMvc.perform(get("/api/users/1"))
//                .andExpect(status().isForbidden());
//    }

//    @Test
//    @WithMockUser(roles = "GUEST")
//    void owner_canAccess() throws Exception {
//
//        // userSecurity is a MockBean
//        when(userSecurity.isOwner(1L)).thenReturn(true);
//
//        // userService is a MockBean
//        when(userService.getUserById(1L)).thenReturn(
//                new UserResponse(
//                        1L,                   // id
//                        "Karim Bassel",       // name
//                        "karim@example.com",  // email
//                        "0123456789",         // phone number
//                        Role.GUEST            // role
//                )
//        );
//
//        mockMvc.perform(get("/api/users/1"))
//                .andExpect(status().isOk());
//    }
//
//
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void admin_canAccess_evenifnotowner() throws Exception {
//
//        when(userSecurity.isOwner(1L)).thenReturn(false);
//        when(userService.getUserById(1L)).thenReturn(
//                new UserResponse(
//                        1L,                   // id
//                        "Karim Bassel",       // name
//                        "karim@example.com",  // email
//                        "0123456789",         // phone number
//                        Role.GUEST            // role
//                )
//        );
//
//        mockMvc.perform(get("/api/users/1"))
//                .andExpect(status().isOk());
//    }
}
