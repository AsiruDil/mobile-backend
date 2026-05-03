package com.project.elephant.config;

import com.project.elephant.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    // සර්වර් එක පණගැන්වෙන විට මෙම Config එක Load වෙනවාදැයි බැලීමට (Debug)
    public SecurityConfig() {
        System.out.println("🛡️ SecurityConfig is successfully loaded by Spring Boot Context!");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. CORS සහ CSRF සැකසුම්
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable()) // Python වැනි පිටස්තර ස්ක්‍රිප්ට් සඳහා මෙය අනිවාර්යයෙන් disable කළ යුතුය

                // 2. Session කළමනාකරණය (Stateless)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. Endpoint අවසරයන් (Permissions)
                .authorizeHttpRequests(auth -> auth
                        // කිසිදු බාධාවකින් තොරව (Public) පිවිසිය හැකි තැන්
                        .requestMatchers("/health", "/api/auth/**").permitAll()
                        .requestMatchers("/api/news", "/ws-elephant/**").permitAll()
                        .requestMatchers("/api/danger-zones", "/api/point-locations").permitAll()

                        // 🚨 Python Camera Alert එක සඳහා විශේෂ අවසරය
                        .requestMatchers(HttpMethod.POST, "/api/sightings/alert").permitAll()
                        .requestMatchers("/api/sightings/**").permitAll()

                        // Admin සඳහා පමණක් ඇති තැන්
                        .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/sightings/admin/**").hasAuthority("ROLE_ADMIN")

                        // අනෙකුත් සියලුම Request සඳහා Token එකක් අවශ්‍යයි
                        .anyRequest().authenticated()
                );

        // 4. JWT Filter එක ඇතුළත් කිරීම
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Python ස්ක්‍රිප්ට් එක සහ Vercel ඇප් එක දෙකම වැඩ කිරීමට
        configuration.setAllowedOriginPatterns(Arrays.asList(
                "http://localhost:3000",
                "http://localhost:5173",
                "http://localhost:8081",
                "https://elephantguard.vercel.app",
                "*" // පරීක්ෂණ මට්ටමේදී පමණක් සියල්ලට ඉඩ දෙන්න
        ));

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "User-Agent", "Accept", "Origin"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}