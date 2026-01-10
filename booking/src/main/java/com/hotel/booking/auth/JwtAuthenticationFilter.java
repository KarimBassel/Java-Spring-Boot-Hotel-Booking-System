package com.hotel.booking.auth;

import com.hotel.booking.dto.UserResponse;
import com.hotel.booking.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/*
This filter checks for JWT token validity.
If the token is valid → protected endpoints are accessible to the requester.
*/
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    public JwtAuthenticationFilter(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        String email = null;
        String token = null;

        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7).trim();
            try {
                email = jwtService.extractEmail(token);
            } catch (Exception ignored) {
                // Token is invalid, email will remain null
            }
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserResponse userResponse = userService.getUserbyEmail(email);

            if (userResponse != null && jwtService.validateJwtToken(token)) {

                SimpleGrantedAuthority authority =
                        new SimpleGrantedAuthority("ROLE_" + userResponse.role());

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userResponse,
                                null,
                                Collections.singleton(authority)
                        );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
