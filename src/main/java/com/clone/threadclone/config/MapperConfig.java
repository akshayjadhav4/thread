package com.clone.threadclone.config;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.clone.threadclone.security.jwt.AuthTokenFilter;
import com.clone.threadclone.security.jwt.JwtAuthEntryPoint;
import com.clone.threadclone.security.user.UserProfileDetailsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class MapperConfig {

    private final UserProfileDetailsService userProfileDetailsService;
    private final JwtAuthEntryPoint authEntryPoint;

    private static final List<String> PUBLIC_URLS = List.of(
            "/api/v1/auth/register",
            "/api/v1/auth/login", "/error");

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthTokenFilter authTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
        return authConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthProvider() {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userProfileDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configure HTTP security options
        http.csrf(csrf -> csrf.disable()) // Disable CSRF.
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPoint)) // Set the
                                                                                                    // JwtAuthEntryPoint
                                                                                                    // as the entry
                                                                                                    // point for
                                                                                                    // handling
                                                                                                    // unauthorized
                                                                                                    // access.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Set
                                                                                                              // session
                                                                                                              // management
                                                                                                              // to
                                                                                                              // STATELESS
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_URLS.toArray(new String[0])).permitAll() // Permit the /api/auth/**
                                                                                         // endpoint for all users and
                                                                                         // secure other endpoints.
                        .anyRequest().authenticated());

        // Set the custom AuthenticationProvider and add token filter
        http.authenticationProvider(daoAuthProvider());
        http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
