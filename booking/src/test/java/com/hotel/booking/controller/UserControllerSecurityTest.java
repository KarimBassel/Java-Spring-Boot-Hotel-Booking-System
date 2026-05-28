package com.hotel.booking.controller;

import com.hotel.booking.config.SecurityConfig;
import com.hotel.booking.dto.UserResponse;
import com.hotel.booking.model.Enums.Role;
import com.hotel.booking.security.UserSecurity;
import com.hotel.booking.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
401 Unauthorized → user not authenticated.
403 Forbidden → user authenticated but lacks permission.
 */
@SpringBootTest
/*
Enables MockMvc, a Spring testing utility for HTTP requests without starting a real server.
 */
@AutoConfigureMockMvc(addFilters = true)
/*
Makes sure your custom security configuration is included in the test context.
 */
@Import(SecurityConfig.class)
@ActiveProfiles("test")
class UserControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;
    /*
    MockBean --> Create real beans in SpringBootTest
    But with controller behaviour
    example --> when(userSecurity.isOwner(1L)).thenReturn(false)

    Proccess:

    1. Spring injects these mocks in  the controller
    2. any method call uses these mocks
     */
    @MockBean
    private UserSecurity userSecurity;

    @MockBean
    private UserService userService;

    /* ------------------ GET /api/users ------------------ */
    /*
    Authenticated but un authorized --> 403 Forbidden
     */
    @Test
    /*
    creates authentication object and injects it in SecurityContext during the period of test execution
    Before the test method runs, Spring uses a TestExecutionListener:
    Specifically, WithSecurityContextTestExecutionListener.
    It sets up the SecurityContext for that thread.
    After the test finishes, it clears the SecurityContext automatically, so tests don’t interfere with each other.
     */
    @WithMockUser(roles = "GUEST")
    void guestCannotAccessGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isForbidden());
    }

    /*
    Authenticated & Authorized --> 200 OK
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void adminCanAccessGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk());
    }

    /*
    Authenticated but un authorized --> 403 Forbidden
     */
    @Test
    @WithMockUser(roles = "GUEST")
    void guestNotOwner_cannotAccess() throws Exception {
        when(userSecurity.isOwner(1L)).thenReturn(false);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isForbidden());
    }

    /*
    Authenticated & Authorized --> 200 OK
     */
    @Test
    @WithMockUser(roles = "GUEST")
    void owner_canAccess() throws Exception {
        when(userSecurity.isOwner(1L)).thenReturn(true);
        when(userService.getUserById(1L)).thenReturn(
                new UserResponse(
                        1L,
                        "Karim Bassel",
                        "karim@example.com",
                        "0123456789",
                        "",
                        Role.GUEST

                )
        );

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk());
    }

    /*
    Authenticated & Authorized --> 200 OK
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void admin_canAccess_evenIfNotOwner() throws Exception {
        when(userSecurity.isOwner(1L)).thenReturn(false);
        when(userService.getUserById(1L)).thenReturn(
                new UserResponse(
                        1L,
                        "Karim Bassel",
                        "karim@example.com",
                        "0123456789",
                        "",
                        Role.GUEST
                )
        );
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk());
    }
}
