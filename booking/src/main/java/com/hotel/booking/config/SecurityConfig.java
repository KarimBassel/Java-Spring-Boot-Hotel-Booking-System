package com.hotel.booking.config;

import com.hotel.booking.auth.filters.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//Spring creates this at app startup
@Configuration
//Activates spring security for the application
//responsible for auth, filter chain, endpoints security
@EnableWebSecurity
//responsible for @PreAuthorize, @PostAuthorize, SPEL Checks Activations
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter){
        this.jwtAuthenticationFilter=jwtAuthenticationFilter;
    }
    /*
    Every HTTP request passes through this chain before reaching controllers
    for filtration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //disables session based authentication
        //JWT auth is stateless authentication

        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)

                //Prevents Unauthenticated access
                ///.anonymous(anonymous -> anonymous.disable())
                //Authorization rules
                //register and login will be public endpoints
                //all other endpoints are protected --> requires the JWT token
             .authorizeHttpRequests(auth -> auth
             .requestMatchers("/api/auth/**",
                                     "/api/images/upload",
                                     "/swagger-ui.html",
                                     "/swagger-ui/**",
                                     "/v3/api-docs/**",
                                     //For version checks on deployment
                                     "/version",
                                     //Enable Access to stripe
                                     "/api/payments/webhooks/stripe",
                                     //For CI/CD Health Checks
                                     "/actuator/health",
                                     //E2E Testing data cleanup endpoint
                                     "/api/test/**").permitAll()
              .anyRequest().authenticated()
                )
                //Stateless sessions
                //every request must carry the jwt token
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}

